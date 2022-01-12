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

