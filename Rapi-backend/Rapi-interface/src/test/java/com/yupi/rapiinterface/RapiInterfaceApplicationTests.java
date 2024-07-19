package com.yupi.rapiinterface;

import com.yupi.rapiclientsdk.client.RapiClient;
import com.yupi.rapiclientsdk.modal.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RapiInterfaceApplicationTests {

    @Resource
    private RapiClient rapiClient;

    @Test
    void contextLoads() {
        String result = rapiClient.getNameByGet("yupi");
        User user = new User();
        user.setUsername("rui");
        String result2 = rapiClient.getUserNameByPost(user);
        System.out.println(result);
        System.out.println(result2);
    }

}
