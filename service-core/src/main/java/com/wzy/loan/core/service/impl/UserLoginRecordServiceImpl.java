package com.wzy.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.loan.core.pojo.entity.UserLoginRecord;
import com.wzy.loan.core.mapper.UserLoginRecordMapper;
import com.wzy.loan.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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


    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Override
    public List<UserLoginRecord> selectUserLoginRecordById(Long id) {

        QueryWrapper<UserLoginRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
//        只查最近的50条
        wrapper.orderByDesc("id").last("limit 50");

        List<UserLoginRecord> userLoginRecords = userLoginRecordMapper.selectList(wrapper);
        return userLoginRecords;
    }
}
