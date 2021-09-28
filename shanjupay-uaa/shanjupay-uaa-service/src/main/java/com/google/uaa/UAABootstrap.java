package com.google.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author sohyun
 * @date 2020/11/23 21:55
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UAABootstrap {
    public static void main(String[] args) {
        SpringApplication.run(UAABootstrap.class, args);
    }
}
