package com.wzy.loan.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author：wzy
 * @date：2022/2/4-02-04-17:22
 */
@SpringBootApplication
@ComponentScan({"com.wzy.loan","com.wzy.common"})
public class ServiceCosApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCosApplication.class,args);
    }

}
