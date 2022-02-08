package com.wzy.loan.core.pojo.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wzy.loan.core.mapper.DictMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：wzy
 * @date：2022/1/30-01-30-16:48
 */


@Slf4j

public class DictListener extends AnalysisEventListener<excelDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<excelDTO> list = new ArrayList<excelDTO>();

    private DictMapper service;
    public DictListener(DictMapper demoDAO) {
        this.service = demoDAO;
    }

    @Override
    public void invoke(excelDTO data, AnalysisContext analysisContext) {
//        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
//            写数据到数据库中
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

//        invoke循环到左后可能还剩下小于count的几条，由doafter来做收尾
        saveData();
        LOGGER.info("存储数据库完成！");

    }

    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        service.insertBatch(list);
        LOGGER.info("存储数据库成功！");
    }
}
