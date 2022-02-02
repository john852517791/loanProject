package com.wzy.loan.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.loan.core.easyExcel.DictListener;
import com.wzy.loan.core.easyExcel.excelDTO;
import com.wzy.loan.core.entity.Dict;
import com.wzy.loan.core.mapper.DictMapper;
import com.wzy.loan.core.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importDataFromExcel(InputStream in) {
//        从excel中读取数据并且写入数据库
        EasyExcel.read(in, excelDTO.class, new DictListener(dictMapper)).sheet().doRead();
    }

    @Override
    public List<excelDTO> listDictData() {
        List<Dict> dicts = baseMapper.selectList(null);
//        将dict类型转化为DTO类型
//        创建长度固定的列表
        ArrayList<excelDTO> excelDTOS = new ArrayList<>(dicts.size());
        dicts.forEach(dict->{
//            创建DTO对象
            excelDTO excelDTO = new excelDTO();
//            将dict中与excelDTO相同的属性值复制过去
            BeanUtils.copyProperties(dict,excelDTO);
//            将赋值好的DTO加入列表
            excelDTOS.add(excelDTO);

        });
        return excelDTOS;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
//        为查询设定条件，where parent_id=？
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentId);
//        要对dict对象填充haschildren的属性
        List<Dict> dicts = baseMapper.selectList(dictQueryWrapper);
        dicts.forEach(dict->{
//            若id与其他行数据的parentId相同则设置hasChildren为TRUE
            dict.setHasChildren(hasChildrenOrNot(dict.getId()));
        });
        return dicts;
    }

    private boolean hasChildrenOrNot(Long parentId){
        //        为查询设定条件，where parent_id=？
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentId);
        if(baseMapper.selectCount(dictQueryWrapper)>0){
            return true;
        }else
            return false;
    }
}
