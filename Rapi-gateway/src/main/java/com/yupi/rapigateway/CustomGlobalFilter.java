package com.yupi.rapigateway;

import com.yupi.rapicommon.model.entity.InterfaceInfo;
import com.yupi.rapicommon.model.entity.User;
import com.yupi.rapicommon.service.InnerInterfaceInfoService;
import com.yupi.rapicommon.service.InnerUserInterfaceinfoService;
import com.yupi.rapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yupi.rapiclientsdk.utils.SignUtil.genSign;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    @DubboReference
    private InnerUserInterfaceinfoService innerUserInterfaceinfoService;

    public static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
        //得到请求体
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = INTERFACE_HOST + serverHttpRequest.getPath().value();
        HttpMethod method = serverHttpRequest.getMethod();
        // 1.请求日志
        log.info("请求唯一标识：" + serverHttpRequest.getId());
        log.info("请求唯一方法：" + serverHttpRequest.getMethod());
        log.info("请求路径：" + path);
        log.info("请求参数：" + serverHttpRequest.getQueryParams());
        log.info("请求地址：" + serverHttpRequest.getRemoteAddress());
        log.info("请求源地址：" + serverHttpRequest.getLocalAddress().getHostString());

        //得到初始化响应体
        ServerHttpResponse response = exchange.getResponse();

        // 2.访问控制（黑白名单）
        if(!IP_WHITE_LIST.contains(serverHttpRequest.getLocalAddress().getHostString())){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // 3.鉴权（accessKey,secretKey）
        HttpHeaders headers = serverHttpRequest.getHeaders();
//        System.out.println(headers);
        String accessKey = headers.getFirst("accessKey");
//        System.out.println("请求体： " + body);
        String timestamp = headers.getFirst("Timestamp");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
//        String body = headers.getFirst("body");
        String body = headers.getFirst("body");
        //TODO 实际应该去数据库中查，看是否已经分配给用户
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        }catch (Exception e){
            log.error("getinvokeUser error",e);
        }
        if(invokeUser == null){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

//        if(!accessKey.equals("yupi")){
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }

        if (Long.parseLong(nonce) > 10000L) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //校验时间戳，不能超过当前时间5分钟
        //获取当前时间戳
        long currentTime = System.currentTimeMillis() / 1000;
        long differenceInSeconds  = currentTime - Long.parseLong(timestamp);
        if(differenceInSeconds >5*60){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //从数据库中查到的secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = genSign(body, secretKey);
        if(!serverSign.equals(sign) || sign == null){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // 4.判断请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceinfo(path, String.valueOf(method));
        }catch (Exception e){
            log.error("getinterfaceinfo error",e);
        }
        if(interfaceInfo == null){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //TODO 判断是否有调用次数


        // 5.请求转发，调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);
//        log.info("响应：" + response.getStatusCode());
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());

    }


    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            innerUserInterfaceinfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }
}