package com.wzy.loan.base.config;/*
 *@Auther: wzy
 *@Date:2021/12/3 -12-03
 *@Descreption: com.wzy.loan.base.config
 *@Version: 1.0
 */


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
public class Swagger2Config{



    @Bean
    public Docket AdminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台管理接口")
                //接口管理分组
                .apiInfo(adminApiInfo())
                //加载adminApiInfo()方法中对文档的补充信息
                .select()
//                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .paths(PathSelectors.regex("/admin/.*"))
//                限定某些controllerurl之下的mapping为这一组的接口
                .build();
    }


    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("SRB后台管理系统API文档")
                .description("描述了后台管理系统各个模块的接口调用方式")
                .version("1.0").contact(new Contact("john","暂无","852517791@qq.com"))
                .build();
    }

    @Bean
    public Docket WebApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("网页接口")
                .apiInfo(adminApiInfo())
                .select()
//                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .paths(PathSelectors.regex("/api/.*"))
                .build();
    }
}
