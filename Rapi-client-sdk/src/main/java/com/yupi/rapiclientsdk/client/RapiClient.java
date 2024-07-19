package com.yupi.rapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.rapiclientsdk.modal.User;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.yupi.rapiclientsdk.utils.SignUtil.genSign;


/**
 * 调用第三方api的接口
 */
public class RapiClient {

    private String accessKey;
    private String secretKey;

    public RapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        //可以单独传入http参数，这样参数会自动做url编码，拼接到url上
        String result = HttpUtil.get("http://localhost:8890/api/name/", paramMap);
        System.out.println(result);
        return result;
    }


    public String getNameByPost( String name){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        //可以单独传入http参数，这样参数会自动做url编码，拼接到url上
        String result = HttpUtil.post("http://localhost:8890/api/name/", paramMap);
        System.out.println(result);
        return result;
    }


    public String getUserNameByPost( User user){
        String jsonStr = JSONUtil.toJsonStr(user);
        //向api地址发送 http请求，向调用api发送请求
        HttpResponse result = HttpRequest.post("http://localhost:8890/api/name/user")
                .charset(StandardCharsets.UTF_8)
                .addHeaders(getHeaderMap(jsonStr))
                .body(jsonStr)
                .execute();

//        System.out.println(result.getStatus());
//        System.out.println(result.body());
        return result.body();
    }

    private Map<String,String> getHeaderMap(String body){
        Map<String,String > hashMap = new HashMap<>();
        hashMap.put("accessKey",accessKey);
//        这里只是演示，实际一定不能发送出去滴
//        hashMap.put("secretKey",secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4)); //四位数的随机数
        hashMap.put("body",body);  //请求体
        hashMap.put("Timestamp", String.valueOf(System.currentTimeMillis()/1000));  //当前时间戳s
        hashMap.put("sign",genSign(body,secretKey)); //用密钥对请求体进行签名加密
        return hashMap;
    }


}
