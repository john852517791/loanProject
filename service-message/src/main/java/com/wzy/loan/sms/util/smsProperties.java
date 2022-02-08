package com.wzy.loan.sms.util;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author：wzy
 * @date：2022/2/6-02-06-17:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "rckj")
//用于读取yml中绑定的键值对
public class smsProperties implements InitializingBean {


//    accountSId: 8a216da87e7baef8017ebad228480764 #自己的accountSId
//    accountToken: 21b7d04e37bc4583976d909d9267a9d5 #自己的accountToken
//    appId: 21b7d04e37bc4583976d909d9267a9d5 #自己的accountToken
//    serverIp: app.cloopen.com  #默认都是这个
//    serverPort: 8883  #默认都是这个

    private String accountSId;
    private String accountToken;
    private String appId;
    private String serverIp;
    private String serverPort;

    public static String ACCOUNTS_ID;
    public static String ACCOUNT_TOKEN;
    public static String APP_ID;
    public static String SERVER_IP;
    public static String SERVER_PORT;


//    容器自动填充且配置文件被读取之后对变量进行赋值
    @Override
    public void afterPropertiesSet() throws Exception {
        ACCOUNTS_ID = accountSId;
        ACCOUNT_TOKEN = accountToken;
        APP_ID = appId;
        SERVER_IP = serverIp;
        SERVER_PORT = serverPort;

    }
}
