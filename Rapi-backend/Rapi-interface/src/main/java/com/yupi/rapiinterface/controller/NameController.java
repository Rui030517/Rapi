package com.yupi.rapiinterface.controller;
import com.yupi.rapiclientsdk.modal.User;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

import static com.yupi.rapiclientsdk.utils.SignUtil.genSign;


/**
 * 名称api
 *
 * @author Rui
 */
@RestController("")
@RequestMapping("/name")
public class NameController {


    @GetMapping("/get")
    public String getNameByGet(String name){

        return "Get 你的名字是：" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name){
        return "Post 你的名字是：" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest httpServletRequest){
//        String accessKey = httpServletRequest.getHeader("accessKey");
//        String body = httpServletRequest.getHeader("body");
//        String timestamp = httpServletRequest.getHeader("Timestamp");
//        String sign = httpServletRequest.getHeader("sign");
//        String nonce = httpServletRequest.getHeader("nonce");
//
//        //TODO 实际应该去数据库中查，看是否已经分配给用户
//        if(!accessKey.equals("yupi")){
//            throw new RuntimeException("无权限");
//        }
//
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }

        //校验时间戳，不能超过当前时间5分钟
        //获取当前时间戳
//        long currentTime = System.currentTimeMillis() / 1000;
//        long differenceInSeconds  = currentTime - Long.parseLong(timestamp);
//        if(differenceInSeconds >5*60){
//            throw new RuntimeException("无权限");
//        }
//
//        //实际情况是从数据库中查到的secretKey
//        String serverSign = genSign(body, "abcdefgh");
//        if(!serverSign.equals(sign)){
//            throw new RuntimeException("无权限");
//        }

        return "Post 用户的名字是：" + user.getUsername();
    }

}
