package com.yupi.rapiclientsdk;

import com.yupi.rapiclientsdk.client.RapiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("rapi.client")
@Data
@ComponentScan
public class RapiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public RapiClient rapiClient(){
        return new RapiClient(accessKey,secretKey);
    }

}
