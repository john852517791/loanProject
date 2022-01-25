package com.wzy.loan.core.controller.admin;/*
 *@Auther: wzy
 *@Date:2021/12/3 -12-03
 *@Descreption: com.wzy.loan.core.controller.admin
 *@Version: 1.0
 */

import com.wzy.common.exception.Assert;
import com.wzy.common.exception.BusinessException;
import com.wzy.common.result.ResponseEnum;
import com.wzy.loan.core.entity.IntegralGrade;
import com.wzy.loan.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import com.wzy.common.result.R;
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
    public R listall(){
        return R.ok().Data("list",integralGradeService.list()).message("获取列表成功");
    }

    @ApiOperation(value = "根据id删除一行积分等级",notes = "逻辑删除")
    @DeleteMapping("/delete/{id}")
    public R removeByID(
            @ApiParam(value = "要删除的数据id",example = "1")
            @PathVariable long id
    ){

        if(integralGradeService.removeById(id)){
            return R.ok().message("删除成功");
        }else {
            return R.error().message("删除失败");

        }
//        return integralGradeService.removeById(id);
    }

    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){

//        抛异常示例
//        if(integralGrade.getBorrowAmount()==null){
//            throw  new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
//        }
        Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);


        if(integralGradeService.save(integralGrade)){
            return R.ok().message("添加成功");
        }else {
            return R.error().message("添加失败");
        }
    }

    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(@PathVariable long id){
        IntegralGrade byId = integralGradeService.getById(id);
        if(byId!=null){
            return R.ok().Data("record",byId).message("获取成功");
        }else {
            return R.error().message("数据获取失败");
        }
    }


    @ApiOperation("修改积分等级")
    @PutMapping("/update")
    public R updateById(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){

        if(integralGradeService.updateById(integralGrade)){
            return R.ok().message("更新成功");
        }else {
            return R.error().message("更新失败");
        }
    }


}
