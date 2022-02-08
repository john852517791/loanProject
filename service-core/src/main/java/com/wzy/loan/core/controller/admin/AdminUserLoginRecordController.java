package com.wzy.loan.core.controller.admin;

import com.wzy.common.result.R;
import com.wzy.loan.core.mapper.UserLoginRecordMapper;
import com.wzy.loan.core.pojo.entity.UserLoginRecord;
import com.wzy.loan.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author：wzy
 * @date：2022/2/8-02-08-22:34
 */
@RestController
@Api(tags = "用户管理")
@CrossOrigin
@Slf4j
@RequestMapping("/admin/core/userLoginRecord")
public class AdminUserLoginRecordController {

    @Resource
    private UserLoginRecordService userLoginRecordService;


    @ApiOperation("根据id查询用户日志")
    @GetMapping("/showLoginRecord/{id}")
    public R showLoginRecord(
            @ApiParam(value = "日志查询的用户id",required = true)
            @PathVariable Long id
    ){

        List<UserLoginRecord> loginRecord = userLoginRecordService.selectUserLoginRecordById(id);

        return R.ok().Data("loginRecord",loginRecord);
    }

}
