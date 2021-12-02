package com.wzy.loan.base.config;/*
 *@Auther: wzy
 *@Date:2021/12/3 -12-03
 *@Descreption: com.wzy.loan.base.config
 *@Version: 1.0
 */

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config{
    @Bean
    public Docket AdminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台管理接口")
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }
    @Bean
    public Docket WebApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("网页接口")
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }
}
