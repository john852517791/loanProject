package com.wzy.loan.core.controller.api;


import com.wzy.common.exception.Assert;
import com.wzy.common.result.R;
import com.wzy.common.result.ResponseEnum;
import com.wzy.common.utils.RegexValidateUtils;
import com.wzy.loan.base.utils.jwtUtils;
import com.wzy.loan.core.pojo.vo.UserInfoVO;
import com.wzy.loan.core.pojo.vo.loginVO;
import com.wzy.loan.core.pojo.vo.registerVO;
import com.wzy.loan.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.temporal.Temporal;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@RestController
@Api(tags = "用户接口")
@CrossOrigin
@Slf4j
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisTemplate redisTemplate;


    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R register(
            @RequestBody registerVO registerVO
            ){

//        手机号、密码的后端检查，是为了防止有人绕过前端恶意攻击
//        检查手机号是否为空
        Assert.notEmpty(registerVO.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
//        检查密码是否为空
        Assert.notEmpty(registerVO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
//        检查验证码是否为空
        Assert.notEmpty(registerVO.getCode(),ResponseEnum.CODE_NULL_ERROR);
//        检查手机号是否复合规范
        Assert.isTrue(RegexValidateUtils.checkCellphone(registerVO.getMobile()),ResponseEnum.MOBILE_ERROR);


//        检查验证码是否正确
        String code = (String) redisTemplate.opsForValue().get("srb:sms:code" + registerVO.getMobile());
        Assert.equals(code,registerVO.getCode(), ResponseEnum.CODE_ERROR);


//        正式注册账号
        userInfoService.register(registerVO);

        return R.ok().message("注册成功");
    }



//    用户登录
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R register(
            @RequestBody loginVO loginVO,
            HttpServletRequest request
            ){
//        首先做验证
        Assert.notEmpty(loginVO.getMobile(),ResponseEnum.MOBILE_ERROR);
        Assert.notEmpty(loginVO.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);

//        获取请求来源的ip地址
        String ip = request.getRemoteAddr();

//        调用service方法做登录验证
        UserInfoVO userInfoVo = userInfoService.login(loginVO,ip);

//        返回正确结果，并且将数据封装为UserInfoVO返回给前端
        return R.ok().Data("userInfo",userInfoVo);
    }

    //    用户登录
    @ApiOperation("token检查")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request){

//        从http请求头部获取token字段
        String token = request.getHeader("token");
//        检查token是否合法
        boolean result = jwtUtils.checkToken(token);

        if(result){
            return R.ok();
        }else {
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }

    }




}

