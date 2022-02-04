package com.wzy.loan.oss.controller.api;

import com.wzy.common.exception.BusinessException;
import com.wzy.common.result.R;
import com.wzy.common.result.ResponseEnum;
import com.wzy.loan.oss.servce.fileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author：wzy
 * @date：2022/2/4-02-04-18:54
 */

@RestController
@Api(tags = "南极贱畜文件管理")
@CrossOrigin
@RequestMapping("/api/oss/file")
public class fileController {


    @Resource
    private fileService fileService;


    @ApiOperation("文件上传至腾讯云")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(value = "上传的文件",required = true)
            @RequestPart(value = "file",required = true)
            MultipartFile file,
            @ApiParam(value = "模块",required = true)
            @RequestParam("module")
            String module
    ){
        try {

//            从前端传来的文件中获取文件流
            InputStream inputStream = file.getInputStream();
//            获取文件的原名
            String originalFilename = file.getOriginalFilename();
//            调用service方法上传文件
            String url = fileService.upload(inputStream, module, originalFilename);
//             若上传成功则返回文件在云端的url
            return R.ok().message("文件上传成功").Data("url",url);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }


    @ApiOperation("删除单个文件")
    @DeleteMapping("/remove")
    public R remove(
            @ApiParam(value = "根据url删除文件",required = true)
            @RequestParam("url")
                    String url
    ){
        fileService.removeFile(url);
        return R.ok().message("文件删除成功");

    }

}
