package com.wzy.loan.sms.controller.api;

import com.wzy.common.exception.Assert;
import com.wzy.common.result.R;
import com.wzy.common.result.ResponseEnum;
import com.wzy.loan.sms.service.smsService;
import com.wzy.common.utils.RandomUtils;
import com.wzy.common.utils.RegexValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author：wzy
 * @date：2022/2/7-02-07-10:26
 */

@RestController
@Api(tags = "联容云短信")
@CrossOrigin
@RequestMapping("/api/sms")
public class smsController {
    @Resource
    private smsService smsService;
    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("短信发送")
    @GetMapping("/send/{phoneNumber}")
    public R sentMessage(
            @ApiParam(value = "手机号",required = true)
            @PathVariable String phoneNumber
    ){

//        手机号不能为空
        Assert.notEmpty(phoneNumber, ResponseEnum.MOBILE_NULL_ERROR);
//         检查手机号是否合法
        Assert.isTrue(RegexValidateUtils.checkCellphone(phoneNumber),ResponseEnum.MOBILE_ERROR);


//        生成随机验证码
        Map<String, Object> map = new HashMap<>();
        String fourBitRandom = RandomUtils.getFourBitRandom();
        map.put("code", fourBitRandom);

//              调用service方法发送验证码
//        smsService.send(phoneNumber,map);

//        将验证码存入redis
        redisTemplate.opsForValue().set("srb:sms:code"+phoneNumber,fourBitRandom,5, TimeUnit.MINUTES);

        return R.ok().message("短信发送成功");
    }

}
