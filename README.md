# loanProject
尚融宝

以下是个人学习文档









# lombok

添加lombok依赖

```java
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```



在实体类的类名上添加@Data注解

可以省去写构造方法与getter、setter方法

# mybatis-plus

添加依赖

```java
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus.version}</version>
</dependency>
```

配合springboot

## BaseMapper接口

我们自己创建的mapper接口可以直接继承basemapper接口

再填上实体类的泛型即可

如下

```java
public interface UserMapper extends BaseMapper<User> {

}
```

basemapper接口里提供了基础方法

到时候service可直接调用



## 配置类

搞一个配置类

```java
@Configuration
@MapperScan("com.wzy.loan.core.mapper")
public class MybatisPlusConfig {
}
```

扫描mapper的全包名

## wrapper

wrapper类的继承关系

![image-20220119150658834](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220119150658834.png)



### queryWrapper

为查询、删除、修改提供复合条件，相当于sql语句中where跟的条件

再使用mapper的相应方法，传入wrapper

```java
QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.like("username","?");
        //即添加条件——username这一列数据中包含？的数据

//        wrapper支持链式编程
        dictQueryWrapper
                .like("username","?")
                .between("age",20,50)
                .isNotNull("email")
                .orderByDesc("age").orderByAsc("id");
        List<Dict> dicts = mapper.selectList(dictQueryWrapper);
        mapper.delete(dictQueryWrapper);
```



## 注解

TableName

实体类映射表名

TableId(value,type)

表主键映射

TableField(value)

表非主键列名

TableLogic

标记逻辑删除字段



# swagger



1、导包，新版本是swagger 的springboot starter

旧版本的配置

```xml
<!--            <dependency>-->
<!--                <groupId>io.springfox</groupId>-->
<!--                <artifactId>springfox-swagger2</artifactId>-->
<!--                <version>${swagger.version}</version>-->
<!--            </dependency>-->
<!--            &lt;!&ndash;swagger ui&ndash;&gt;-->
<!--            <dependency>-->
<!--                <groupId>io.springfox</groupId>-->
<!--                <artifactId>springfox-swagger-ui</artifactId>-->
<!--                <version>${swagger.version}</version>-->
<!--            </dependency>-->
<!--            &lt;!&ndash;swagger-bootstrap-ui&ndash;&gt;-->
<!--            <dependency>-->
<!--                <groupId>com.github.xiaoymin</groupId>-->
<!--                <artifactId>swagger-bootstrap-ui</artifactId>-->
<!--                <version>${swagger-bootstrap-ui.version}</version>-->
<!--            </dependency>-->
```

新版本

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>${swagger.version}</version>
</dependency>
```

2、配置

3.0.0的starter版本后只需要configuration一个注解配置即可

```java
@Configuration
@EnableSwagger2
public class Swagger2Config{



    @Bean
    public Docket AdminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("后台管理接口")
                //接口管理分组
                .apiInfo(adminApiInfo())
                //加载adminApiInfo()方法中对文档的补充信息
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
//                限定某些controllerurl之下的mapping为这一组的接口
                .build();
    }


    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("SRB后台管理系统API文档")
                .description("描述了后台管理系统各个模块的接口调用方式")
                .version("1.0").contact(new Contact("john","暂无","852517791@qq.com"))
                .build();
    }
}
```



3、在controller中使用

```java
@Api(tags = "积分等级管理")
@CrossOrigin
@RequestMapping("/admin/core/integralGrade")
@RestController
public class AdminIntegralGradeController {


    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("查询所有积分等级")
    @GetMapping("/list")
    public List<IntegralGrade> listall(){
        return integralGradeService.list();
    }

    @ApiOperation(value = "根据id删除一行积分等级",notes = "逻辑删除")
    @DeleteMapping("/delete/{id}")
    public boolean removeByID(
            @ApiParam(value = "要删除的数据id",example = "1")
            @PathVariable long id
    ){
        return integralGradeService.removeById(id);
    }

}
```



```
@Api(tags = "积分等级管理")
```

使用在controller类上，用于区分不同类的api

```
@ApiOperation(value ="查询所有积分等级",notes = "逻辑删除")
```

使用在controller中的方法上，区分api，可添加补充信息

```
@ApiParam(value = "要删除的数据id",example = "1")
```

使用在controller中方法的参数上，标记形式参数类型，可添加补充的样例信息



4、访问swagger

旧版本需要在端口号后加/swagger-ui.html来访问

[如]: http://localhost:8110/swagger-ui.html

新版本则是

[如]: http://localhost:8110/swagger-ui



# 统一返回结果

为了防止歧义，response中除了返回数据之外还需要给定一些内容

这个项目所用的是包含code、message和data三种数据的返回类型R

其中code和message用枚举类型构建，data并入到返回类型R中

## 状态码枚举类型实现

```java
public enum ResponseEnum {

    SUCCESS(0,"成功"),
    ERROR(-1,"失败");
    private Integer code;
    private String message;


}

```



根据需求多设定一些code和对应的message

## 返回类型R实现

R中包含code、message和data

data就用hashmap实现

方法设计——首先私有化构造方法，将code和message以静态的方式注入，

```java
@Data
public class R {

    private Integer code;
    private String message;
    private Map<String,Object> data=new HashMap<>();

    private R(){

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
```







