package com.wzy.loan.core;/*
 *@Auther: wzy
 *@Date:2021/12/3 -12-03
 *@Descreption: com.wzy.loan
 *@Version: 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.wzy.loan"})
public class ServiceCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCoreApplication.class,args);
    }
}
