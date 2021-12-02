package com.wzy.loan.core.service.impl;

import com.wzy.loan.core.entity.UserLoginRecord;
import com.wzy.loan.core.mapper.UserLoginRecordMapper;
import com.wzy.loan.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

}
