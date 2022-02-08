package com.wzy.loan.core.service;

import com.wzy.loan.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

//    查询最近的50条用户记录
    List<UserLoginRecord> selectUserLoginRecordById(Long id);


}
