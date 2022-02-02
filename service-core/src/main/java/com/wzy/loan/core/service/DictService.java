package com.wzy.loan.core.service;

import com.wzy.loan.core.easyExcel.excelDTO;
import com.wzy.loan.core.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
public interface DictService extends IService<Dict> {
    void importDataFromExcel(InputStream in);

    List<excelDTO> listDictData();

    List<Dict> listByParentId(Long parentId);
}
