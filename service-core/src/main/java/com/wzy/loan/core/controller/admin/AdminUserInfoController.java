package com.wzy.loan.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzy.common.exception.Assert;
import com.wzy.common.result.R;
import com.wzy.common.result.ResponseEnum;
import com.wzy.common.utils.RegexValidateUtils;
import com.wzy.loan.base.utils.jwtUtils;
import com.wzy.loan.core.pojo.entity.UserInfo;
import com.wzy.loan.core.pojo.entity.UserLoginRecord;
import com.wzy.loan.core.pojo.query.userInfoQuery;
import com.wzy.loan.core.pojo.vo.UserInfoVO;
import com.wzy.loan.core.pojo.vo.loginVO;
import com.wzy.loan.core.pojo.vo.registerVO;
import com.wzy.loan.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@RestController
@Api(tags = "用户管理")
@CrossOrigin
@Slf4j
@RequestMapping("/admin/core/userInfo")
public class AdminUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisTemplate redisTemplate;


//    get方法不能用requestbody注解，因为get压根没有请求体，但其它方法可以
    @ApiOperation("获取用户分页列表")
    @GetMapping("/list/{page}/{limit}")
    public R listByCondition(
            @ApiParam(value = "每页的记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(value = "查询条件",required = false)
            userInfoQuery userInfoQuery
            //查询的条件可以没有，即查询全部
            ){

//        创建分页对象
        Page<UserInfo> pageParam = new Page<UserInfo>(page, limit);

//        调用service方法来获取
        IPage<UserInfo> pageModel=userInfoService.listPage(pageParam,userInfoQuery);

//        返回正确结果，将查询到的pageModel返回
        return R.ok().Data("pageModel",pageModel);

    }

    @ApiOperation("锁定或解锁")
    @PutMapping("/setLockStatus/{id}/{status}")
    public R setLockStatus(
            @ApiParam(value = "要改状态的id",required = true)
            @PathVariable Long id,
            @ApiParam(value = "要改成什么状态",required = true)
            @PathVariable Integer status){
        userInfoService.setLockStatus(id,status);
        return R.ok().message(status==1? "解锁成功": "锁定成功");
    }





}

