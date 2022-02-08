package com.wzy.loan.sms.service;

import java.util.Map;

/**
 * @author：wzy
 * @date：2022/2/7-02-07-10:13
 */
public interface smsService {

    /**
     * 发送短信
     * @param phoneNumber  目标手机号码
     * @param params
     */
    void send(String phoneNumber, Map<String,Object> params) ;


}
