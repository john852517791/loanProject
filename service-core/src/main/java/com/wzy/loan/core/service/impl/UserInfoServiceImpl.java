package com.wzy.loan.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.xml.bind.v2.TODO;
import com.wzy.common.exception.Assert;
import com.wzy.common.result.ResponseEnum;
import com.wzy.common.utils.MD5;
import com.wzy.loan.base.utils.jwtUtils;
import com.wzy.loan.core.mapper.UserAccountMapper;
import com.wzy.loan.core.mapper.UserLoginRecordMapper;
import com.wzy.loan.core.pojo.entity.UserAccount;
import com.wzy.loan.core.pojo.entity.UserInfo;
import com.wzy.loan.core.mapper.UserInfoMapper;
import com.wzy.loan.core.pojo.entity.UserLoginRecord;
import com.wzy.loan.core.pojo.query.userInfoQuery;
import com.wzy.loan.core.pojo.vo.UserInfoVO;
import com.wzy.loan.core.pojo.vo.loginVO;
import com.wzy.loan.core.pojo.vo.registerVO;
import com.wzy.loan.core.service.UserAccountService;
import com.wzy.loan.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.JwtBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;


    @Resource
    private UserAccountMapper userAccountMapper;

    @Transactional(rollbackFor = Exception.class)
//    添加事务属性，若有异常则回滚
    @Override
    public void register(registerVO registerVO) {

//        检查此账号是否已经被注册过了
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
//        查询数据库中与此号码相同的行数
        userInfoQueryWrapper.eq("mobile",registerVO.getMobile());
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        Assert.isTrue(0==count, ResponseEnum.MOBILE_EXIST_ERROR);

//        插入用户信息记录，userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVO.getUserType());
        userInfo.setNickName(registerVO.getMobile());
        userInfo.setName(registerVO.getMobile());
        userInfo.setMobile(registerVO.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword()));
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
//        设置用户默认头像
        userInfo.setHeadImg(UserInfo.DEFAULT_AVATAR);
        baseMapper.insert(userInfo);

//插入用户账户记录：user_account
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(userInfo.getId());
            userAccountMapper.insert(userAccount);



    }

    @Override
    public UserInfoVO login(loginVO loginVO, String ip) {


//        此账号的用户是否存在【判断账号和账号类型】
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq("mobile",loginVO.getMobile())
                .eq("user_type",loginVO.getUserType());
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);

//        密码是否正确
        Assert.equals(userInfo.getPassword(),MD5.encrypt(loginVO.getPassword()),ResponseEnum.LOGIN_PASSWORD_ERROR);
//        用户是否被锁定
        Assert.equals(userInfo.getStatus(),UserInfo.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);
//        记录登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);


//        生成token
        String token = jwtUtils.createToken(userInfo.getId(), userInfo.getName());

//        将jwt装入UserInfoVO
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setMobile(userInfo.getMobile());
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setToken(token);
        userInfoVO.setUserType(userInfo.getUserType());

//        返回UserInfoVO
        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, userInfoQuery userInfoQuery) {

//        若前端没传条件，则直接返回查询所有
        if (userInfoQuery == null){
            return baseMapper.selectPage(pageParam,null);
        }
//        若有查询条件，则接收条件组装wrapper
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .like(StringUtils.isNotBlank(userInfoQuery.getMobile()),"mobile",userInfoQuery.getMobile())
                .like(userInfoQuery.getStatus()!=null,"status",userInfoQuery.getStatus())
                .like(userInfoQuery.getUserType()!=null,"user_type",userInfoQuery.getUserType());

        return  baseMapper.selectPage(pageParam,userInfoQueryWrapper);

    }

    @Override
    public void setLockStatus(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }


}
