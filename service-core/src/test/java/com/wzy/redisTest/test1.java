package com.wzy.redisTest;

import com.wzy.loan.core.ServiceCoreApplication;
import com.wzy.loan.core.entity.Dict;
import com.wzy.loan.core.mapper.DictMapper;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author：wzy
 * @date：2022/2/2-02-02-14:31
 */


@SpringBootTest(classes = ServiceCoreApplication.class)
@RunWith(SpringRunner.class)
public class test1 {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DictMapper dictMapper;

    @Test
    public void saveDict(){

        Dict dict = dictMapper.selectById(1);
//        System.out.println(dict);
        redisTemplate.opsForValue().set("dict",dict,5, TimeUnit.MINUTES);
    }

    @Test
    public void get(){
        Dict dict = (Dict) redisTemplate.opsForValue().get("dict");
        System.out.println(dict);
    }

}
