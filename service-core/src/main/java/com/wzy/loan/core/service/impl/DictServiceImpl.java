package com.wzy.loan.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.loan.core.pojo.easyExcel.DictListener;
import com.wzy.loan.core.pojo.easyExcel.excelDTO;
import com.wzy.loan.core.pojo.entity.Dict;
import com.wzy.loan.core.mapper.DictMapper;
import com.wzy.loan.core.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author john9
 * @since 2021-12-03
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictMapper dictMapper;
    @Resource
    private RedisTemplate redisTemplate;


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
//        引入redis后，先从redis中找，
//        若redis中没有，则到数据库中查询，
//        查询后将数据存到redis中
//        最终返回数据

//        先设计key
//        多级列表数据，各个级别有各自的parentid，以这个为标识


//        尝试从redis中查找
        try {
            List<Dict> o = (List<Dict>) redisTemplate.opsForValue().get("srb:admin:dictList:" + parentId);
            if(o!=null){
                return o;
            }
        } catch (Exception e) {
            log.error("redis异常"+ ExceptionUtils.getStackTrace(e));
        }


//        redis查找失败后查询数据库
//        为查询设定条件，where parent_id=？
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentId);
//        要对dict对象填充haschildren的属性
        List<Dict> dicts = baseMapper.selectList(dictQueryWrapper);
        dicts.forEach(dict->{
//            若id与其他行数据的parentId相同则设置hasChildren为TRUE
            dict.setHasChildren(hasChildrenOrNot(dict.getId()));
        });


//        查询完数据库后将数据存入redis
        try {
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("srb:admin:dictList:"+parentId,dicts,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis异常"+ ExceptionUtils.getStackTrace(e));
        }

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
