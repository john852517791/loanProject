package com.wzy.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
//import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author：wzy
 * @date：2022/1/29-01-29-17:56
 */
@Slf4j
public class exelTest {

    @Test
    public void write1(){
        String filename = "G:/test"+System.currentTimeMillis()+".xlsx";
        EasyExcel.write(filename, DemoData.class).sheet("模板").doWrite(data());
    }

    @Test
    public void write2(){
        String filename = "G:/test"+System.currentTimeMillis()+".xls";
        EasyExcel.write(filename, DemoData.class).excelType(ExcelTypeEnum.XLS).sheet("模板").doWrite(data());
    }


    @Test
    public void read(){
        String fileName = "G:\\test1643531218761.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();

    }




    private List<DemoData> data(){
        List<DemoData> studentDTOS = new ArrayList<>();
        for (int i=0;i<10;i++){
            DemoData studentDTO = new DemoData();
            studentDTO.setDate(new Date());
            studentDTO.setDoubleData(8.002);
            studentDTO.setString("wzyaljx"+i);
            studentDTOS.add(studentDTO);
        }
        return studentDTOS;
    }
}




// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
class DemoDataListener extends AnalysisEventListener<DemoData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataListener.class);

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(DemoData data, AnalysisContext context) {
//        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));

    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成！");
    }


}