package com.wzy.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：wzy
 * @date：2022/1/24-01-24-19:04
 */
@Data
public class R {

    private Integer code;
    private String message;
    private Map<String,Object> data=new HashMap<>();

    private R(){

    }
//    成功的返回结果
    public static R ok(){
        R r = new R();
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMessage(ResponseEnum.SUCCESS.getMessage());
        return r;
    }
//    失败的返回结果
    public static R error(){
        R r = new R();
        r.setCode(ResponseEnum.ERROR.getCode());
        r.setMessage(ResponseEnum.ERROR.getMessage());
        return r;
    }

    public static R setResult(ResponseEnum responseEnum){
        R r = new R();
        r.setCode(responseEnum.getCode());
        r.setMessage(responseEnum.getMessage());
        return r;
    }

//    将查询数据放到R返回对象中
    public R Data(String key,Object value){
        this.data.put(key,value);
        return this;
    }
//    重构一下，若返回数据本来就是hashmap
    public R Data(Map<String,Object> map){
        this.setData(map);
        return this;
    }





    //    个性化设置message
    public R message(String message){
        this.setMessage(message);
        return this;
    }
    //    个性化设置code
    public R code(Integer code){
        this.setCode(code);
        return this;
    }

}
