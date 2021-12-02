package com.wzy.loan.core.service.impl;

import com.wzy.loan.core.entity.UserInfo;
import com.wzy.loan.core.mapper.UserInfoMapper;
import com.wzy.loan.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
