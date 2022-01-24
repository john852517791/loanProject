package com.wzy.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wzy.loan.core.entity.Dict;
import com.wzy.loan.core.mapper.DictMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author：wzy
 * @date：2022/1/19-01-19-15:08
 */
@SpringBootTest
public class wrapperTests {
    @Resource
    private DictMapper mapper;



    /*
    *
    * */
    @Test
    public void test1(){
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .like("username","?")
        //即添加条件——username这一列数据中包含？的数据
                .lt("age",20);

//        wrapper支持链式编程
        dictQueryWrapper
                .like("username","?")
                .between("age",20,50)
                .isNotNull("email")
                .orderByDesc("age").orderByAsc("id");
        List<Dict> dicts = mapper.selectList(dictQueryWrapper);
        //查询操作


        int delete = mapper.delete(dictQueryWrapper);
        //删除操作



        Dict dict = new Dict();
        dict.setName("胡汉三");
        dict.setValue(22);
        mapper.update(dict,dictQueryWrapper);
        //修改操作方法1，框架会默认不对传入对象中为空的字段作修改

        UpdateWrapper<Dict> up = new UpdateWrapper<>();
        up.set("name","胡汉三").set("value",22).isNull("name");
        Dict user = new Dict();
        mapper.update(user,up);
        //修改操作方法2


//        查询后只返回特定列的操作
        QueryWrapper<Dict> dictQueryWrapper1 = new QueryWrapper<>();
        dictQueryWrapper1.select("name","value");
        List<Map<String, Object>> maps = mapper.selectMaps(dictQueryWrapper1);
        maps.forEach(System.out::println); 


    }


}
