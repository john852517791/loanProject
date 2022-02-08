package com.wzy.loan.sms.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.wzy.common.exception.BusinessException;
import com.wzy.common.result.ResponseEnum;
import com.wzy.loan.sms.service.smsService;
import com.wzy.loan.sms.util.smsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author：wzy
 * @date：2022/2/7-02-07-10:14
 */

@Service
@Slf4j
public class smsServiceImpl implements smsService {

    @Override
    public void send(String phoneNumber, Map<String, Object> params) {
        CCPRestSmsSDK ccpRestSmsSDK=new CCPRestSmsSDK();
        ccpRestSmsSDK.setAccount(smsProperties.ACCOUNTS_ID,smsProperties.ACCOUNT_TOKEN);
        ccpRestSmsSDK.setAppId(smsProperties.APP_ID);
        ccpRestSmsSDK.setBodyType(BodyType.Type_JSON);
        ccpRestSmsSDK.init(smsProperties.SERVER_IP,smsProperties.SERVER_PORT);


        String[] datas={(String) params.get("code"),"2"};

        HashMap<String, Object> result = ccpRestSmsSDK.sendTemplateSMS(phoneNumber, "1", datas);
        System.out.println("SDKTestGetSubAccounts result=" + result);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
    }
}
