package com.wzy.loan.core.service.impl;

import com.wzy.loan.core.pojo.entity.UserAccount;
import com.wzy.loan.core.mapper.UserAccountMapper;
import com.wzy.loan.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
