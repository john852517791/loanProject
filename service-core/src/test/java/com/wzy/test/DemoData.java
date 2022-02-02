package com.wzy.test;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author：wzy
 * @date：2022/1/30-01-30-16:30
 */@Data
public class DemoData{
    @ExcelProperty("姓名")
    private String string;
    @ExcelProperty("生日")
    private Date date;
    @ExcelProperty("工资")
    private Double doubleData;

}
