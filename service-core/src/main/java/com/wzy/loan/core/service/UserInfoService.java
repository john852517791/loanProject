package com.wzy.loan.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzy.loan.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wzy.loan.core.pojo.entity.UserLoginRecord;
import com.wzy.loan.core.pojo.query.userInfoQuery;
import com.wzy.loan.core.pojo.vo.UserInfoVO;
import com.wzy.loan.core.pojo.vo.loginVO;
import com.wzy.loan.core.pojo.vo.registerVO;

import java.util.List;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
public interface UserInfoService extends IService<UserInfo> {

//    新建用户
    public void register(registerVO registerVO);

    UserInfoVO login(loginVO loginVO, String ip);


    IPage<UserInfo> listPage(Page<UserInfo> pageParam, userInfoQuery userInfoQuery);

    void setLockStatus(Long id, Integer status);


}
