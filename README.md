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



## 1、导包，新版本是swagger 的springboot starter

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

## 2、配置

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



## 3、在controller中使用

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



## 4、访问swagger

旧版本需要在端口号后加/swagger-ui.html来访问

[如]: http://localhost:8110/swagger-ui.html

新版本则是

[如]: http://localhost:8110/swagger-ui/



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



## 统一返回结果的CRUD

主要的改变就在于判断上面

如果调用service方法返回不为空则返回R成功

如下

```java
@ApiOperation("根据id获取积分等级")
@GetMapping("/get/{id}")
public R getById(@PathVariable long id){
    IntegralGrade byId = integralGradeService.getById(id);
    if(byId!=null){
        return R.ok().Data("record",byId).message("获取成功");
    }else {
        return R.error().message("数据获取失败");
    }
}
```

增——postmapping

删——deletemapping

改——putmapping

查——getmapping





# 统一异常处理

## 针对不同异常类的处理模板

```java
@Slf4j
@RestControllerAdvice
public class UnifiedExceptionalHandler {


    @ExceptionHandler({Exception.class})
//    指定针对哪一类异常
    public R handleException(Exception e){
//        可对此方法进行多次重载，修改参数为所需要特殊处理的异常类型
        log.error(e.getMessage(),e);
//        日志打印异常信息
        return R.error();
//        可返回定制化的异常信息,如下
//        return R.setResult(ResponseEnum.ALIYUN_SMS_ERROR);
    }
	@ExceptionHandler(IOException.class)
    public R handleException(IOException e){
        log.error(e.getMessage(),e);
        return R.setResult(ResponseEnum.EXPORT_DATA_ERROR);
    }

}
```



## 自定义异常

目标——使用一个或较少的异常类，以捕获和显示所有的异常信息

how to do——创建自定义异常类（为运行时异常runtimeexception），在程序中出现异常的时候抛出这个异常对象，并在统一异常处理器中捕获自定义异常对象

流程：在controller中判断某种条件，若条件xxx则抛出自定义异常，然后用异常处理器来捕获这个自定义异常

定义一个BusinessException类

```java
@Data
@NoArgsConstructor
public class BusinessException extends RuntimeException {


    //状态码
    private Integer code;

    //错误消息
    private String message;

    /**
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this.message = message;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     */
    public BusinessException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param message 错误消息
     * @param code 错误码
     * @param cause 原始异常对象
     */
    public BusinessException(String message, Integer code, Throwable cause) {
        super(cause);
        this.message = message;
        this.code = code;
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     */
    public BusinessException(ResponseEnum resultCodeEnum) {
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }

    /**
     *
     * @param resultCodeEnum 接收枚举类型
     * @param cause 原始异常对象
     */
    public BusinessException(ResponseEnum resultCodeEnum, Throwable cause) {
        super(cause);
        this.message = resultCodeEnum.getMessage();
        this.code = resultCodeEnum.getCode();
    }


}
```

主要是写构造类，将之前写的枚举类型作为形参传入



在controller中抛出异常

```java
 @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){

//        抛异常示例
        if(integralGrade.getBorrowAmount()==null){
            throw  new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        }
        


        if(integralGradeService.save(integralGrade)){
            return R.ok().message("添加成功");
        }else {
            return R.error().message("添加失败");
        }
    }
```



异常处理器处理

```java
@Slf4j
@RestControllerAdvice
public class UnifiedExceptionalHandler {


    @ExceptionHandler(value = BusinessException.class)
    public R handleException(BusinessException e){
        log.error(e.getMessage(),e);
        return R.error().message(e.getMessage()).code(e.getCode());
    }
}
```

## 断言assert

对逻辑判断的进一步封装【封装为静态方法】

如

```java
@Slf4j
public class Assert {
    /**
     * 断言对象不为空
     * 如果对象obj为空，则抛出异常
     * @param obj 待判断对象
     */
    public static void notNull(Object obj, ResponseEnum responseEnum) {
        if (obj == null) {
            log.info("obj is null...............");
            throw new BusinessException(responseEnum);
        }
    }
}
```

这样一来上面的if判断就可以进一步缩减为

```java
Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
```

## 阶段分类异常

![image-20220126171136425](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220126171136425.png)

处理controller之前的异常就需要另外添加处理

```java
/**
 * Controller上一层相关异常
 */
@ExceptionHandler({
        NoHandlerFoundException.class,
        HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotSupportedException.class,
        MissingPathVariableException.class,
        MissingServletRequestParameterException.class,
        TypeMismatchException.class,
        HttpMessageNotReadableException.class,
        HttpMessageNotWritableException.class,
        MethodArgumentNotValidException.class,
        HttpMediaTypeNotAcceptableException.class,
        ServletRequestBindingException.class,
        ConversionNotSupportedException.class,
        MissingServletRequestPartException.class,
        AsyncRequestTimeoutException.class
})
public R handleServletException(Exception e) {
    log.error(e.getMessage(), e);
    //SERVLET_ERROR(-102, "servlet请求异常"),
    return R.error().message(ResponseEnum.SERVLET_ERROR.getMessage()).code(ResponseEnum.SERVLET_ERROR.getCode());
}
```





# 日志处理



## 日志级别

springboot 的yml配置中

```yml
logging:
  level:
    root: error
```

日志级别由低到高为

trace——debug——info——warn——error

还有all和off



## logback框架

需要配置一个xml文件，名为logback-spring

这个名字springboot框架会自动识别

### property

xml文件中需要配置一些property，做一些映射关系，为主要配置做准备

```xml
<property name="log.path" value="D:/workspace/loanProject/service-core/src/main/resources/log" />

<!--控制台日志格式：彩色日志-->
<!-- magenta:洋红 -->
<!-- boldMagenta:粗红-->
<!-- cyan:青色 -->
<!-- white:白色 -->
<!-- magenta:洋红 -->
<property name="CONSOLE_LOG_PATTERN"
          value="%yellow(%date{yyyy-MM-dd HH:mm:ss}) %highlight([%-5level]) %green(%logger) %msg%n"/>

<!--文件日志格式-->
<property name="FILE_LOG_PATTERN"
          value="%date{yyyy-MM-dd HH:mm:ss} [%-5level] %thread %file:%line %logger %msg%n" />

<!--编码-->
<property name="ENCODING"  value="UTF-8" />
```

### 控制台日志

控制台日志输出的配置标签，其中${}中就是输入之前property配的 name

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        <charset>${ENCODING}</charset>
    </encoder>
</appender>
```



### log文件输出

```xml
<appender name="FILE" class="ch.qos.logback.core.FileAppender">
    <file>${log.path}/log.log</file>
    <append>true</append>
    <encoder>
        <pattern>${FILE_LOG_PATTERN}</pattern>
        <charset>${ENCODING}</charset>
    </encoder>
</appender>
```

### 环境配置选择

分开发环境、测试环境以及生产环境

不同环境可以对日志进行区别处理

主要判断依据是yml配置文件中的spring.profiles.active的属性值【即与springProfile标签的name属性对应】

ref属性对应的使之前appender标签的name属性

```xml
<!-- 开发环境和测试环境 -->
<springProfile name="dev,test">
    <logger name="com.atguigu" level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="ROLLING_FILE" />
    </logger>
</springProfile>

<!-- 生产环境 -->
<springProfile name="prod">
    <logger name="com.atguigu" level="ERROR">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </logger>
</springProfile>
```

### 滚动日志记录

当服务开启，日志就会一直 生成，日志文件也会越来越大，因此就需要策略来控制日志文件大小

这里使用的策略是前一天的日志都会被归档到另外一个日志文件中，此外还可以设置如果log文件大小超过某个阈值，就将过去的日志归档到另外一个文件中【使内存中的日志文件大小稳定在某个阈值之下】



```xml
<appender name="ROLLING_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">

    <!--  要区别于其他的appender中的文件名字  -->
    <file>${log.path}/log-rolling.log</file>
    <encoder>
        <pattern>${FILE_LOG_PATTERN}</pattern>
        <charset>${ENCODING}</charset>
    </encoder>


    <!-- 设置滚动日志记录的滚动策略 -->
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <!-- 日志归档路径以及格式 -->
        <fileNamePattern>${log.path}/info/log-rolling-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <!--归档日志文件保留的最大数量-->
        <maxHistory>15</maxHistory>

        <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
            <maxFileSize>1KB</maxFileSize>
        </timeBasedFileNamingAndTriggeringPolicy>
    </rollingPolicy>

</appender>
```

