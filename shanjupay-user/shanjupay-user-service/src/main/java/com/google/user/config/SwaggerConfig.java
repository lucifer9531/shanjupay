package com.google.user.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sohyun
 * @date 2020/11/16 00:16
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig {

    @Bean
    @SuppressWarnings("all")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("开发者","sohyun","ruixijiejie@gmail.com");
        return new ApiInfoBuilder()
                .description("一个简单且易上手的 微服务 后台管理框架")
                .contact(contact)
                .title("闪聚支付-商户应用API文档")
                .version("1.0.0")
                .build();
    }
}