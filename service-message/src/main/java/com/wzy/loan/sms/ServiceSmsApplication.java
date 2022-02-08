package com.wzy.loan.sms;/*
 *@Auther: wzy
 *@Date:2021/12/3 -12-03
 *@Descreption: com.wzy.loan
 *@Version: 1.0
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.wzy.loan","com.wzy.common"})
public class ServiceSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class,args);
    }
}
