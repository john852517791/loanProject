package com.wzy.loan.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.wzy.common.exception.BusinessException;
import com.wzy.common.result.R;
import com.wzy.common.result.ResponseEnum;
import com.wzy.loan.core.pojo.easyExcel.excelDTO;
import com.wzy.loan.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@RestController
@Api(tags = "数据字典管理")
@CrossOrigin
@Slf4j
@RequestMapping("/admin/core/dict")
public class AdminDictController {

    @Resource
    private DictService dictService;

    @ApiOperation("从excel中导入数据到数据库")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel数据字典文件", required = true)
//            使用requestparam注解会导致swagger页面中无法上传文件
            @RequestPart("file") MultipartFile file){

        try {
            InputStream inputStream = file.getInputStream();
            dictService.importDataFromExcel(inputStream);

            return R.ok().message("数据字典数据批量导入成功");

        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, e);
        }
    }


    @ApiOperation("将数据字典写入Excel并且下载")
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据字典", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), excelDTO.class).sheet("模板").doWrite(dictService.listDictData());
//                                                                                                    这里直接调用list方法，返回的列表类型不是前面指定的DTO类。因此需要重新定义一个方法
    }

    @ApiOperation("获取dict数据")
    @GetMapping("/list")
    public R list()  {
       return R.ok().Data("list",dictService.listDictData()).message("获取dict列表成功");
    }

    @ApiOperation("根据上级id来获取下级数据")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(
            @ApiParam(value = "上级节点的 ID",required = true)
            @PathVariable Long parentId
    )  {
        return R.ok().Data("list",dictService.listByParentId(parentId)).message("获取dict列表成功");
    }


}

