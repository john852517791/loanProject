package com.wzy.loan.core.mapper;

import com.wzy.loan.core.easyExcel.excelDTO;
import com.wzy.loan.core.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
public interface DictMapper extends BaseMapper<Dict> {

    boolean insertBatch(List<excelDTO> a);

}
