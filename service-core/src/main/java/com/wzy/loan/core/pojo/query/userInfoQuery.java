package com.wzy.loan.core.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author：wzy
 * @date：2022/2/8-02-08-11:09
 */


@Data
@ApiModel(description="用户搜索对象")
public class userInfoQuery {
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "1：出借人 2：借款人")
    private Integer userType;
}
