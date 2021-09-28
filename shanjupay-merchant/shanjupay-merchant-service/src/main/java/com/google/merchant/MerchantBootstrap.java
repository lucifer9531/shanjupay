package com.google.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author sohyun
 * @date 2020/11/15 22:39
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MerchantBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(MerchantBootstrap.class, args);
    }
}
