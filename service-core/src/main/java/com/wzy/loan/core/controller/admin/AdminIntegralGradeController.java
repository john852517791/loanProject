package com.wzy.loan.core.controller.admin;/*
 *@Auther: wzy
 *@Date:2021/12/3 -12-03
 *@Descreption: com.wzy.loan.core.controller.admin
 *@Version: 1.0
 */

import com.wzy.loan.core.entity.IntegralGrade;
import com.wzy.loan.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "积分等级管理")
@CrossOrigin
@RequestMapping("/admin/core/integralGrade")
@RestController
public class AdminIntegralGradeController {


    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("查询所有积分等级")
    @GetMapping("/list")
    public List<IntegralGrade> listall(){
        return integralGradeService.list();
    }

    @ApiOperation("根据id删除一行积分等级")
    @DeleteMapping("/delete/{id}")
    public boolean removeByID(@PathVariable long id){
        return integralGradeService.removeById(id);
    }

}
