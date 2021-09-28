package com.google.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author sohyun
 * @date 2020/11/19 22:34
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TransactionBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(TransactionBootstrap.class, args);
    }
}
