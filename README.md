# loanProject
尚融宝

以下是个人学习文档







# 前端页面

## 修改记录

### 导航栏

想修改这一部分的数据

![image-20220126230345968](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220126230345968.png)

按照以下目录寻找

![image-20220126230300776](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220126230300776.png)

修改即可

### 前端框架的目录结构

![image-20220127111913621](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220127111913621.png)

#### node_modules

首先是node_modules，这个文件夹相当于java项目中的jar包文件夹

把项目pull下来之后只需要在命令行中执行npm install就可以自动生成

#### src

然后是最主要的src文件夹，我们需要写的东西全在这个文件夹中

##### utils

此文件夹用于存放一些公共配置和工具类，如baseurl，拦截器等等



##### api

这个文件夹是存放与后端交互的api，即controller中所写的url

样例如下

引入的request中含baseURL以及axios

写法如下，method即为mapping的属性【get、post、put、delete...】

```js
// 引入axios的初始化模块
import request from '@/utils/request'


export default{
    // 定义模块成员
    
    // list方法：获取积分等级列表 
    list(){
        // 调用axios初始化模块，发送ajax请求
        return request({
            url: '/admin/integralGrade/list',
            method: 'get'
        })
    },
    // 根据id删除积分区间
    deleteById(id){
        // 调用axios初始化模块，发送ajax请求
        return request({
            url: '/admin/integralGrade/delete/'+id,
            method: 'delete'
        })
    },

    // 新增积分区间
    save(integralGrade){
        // 调用axios初始化模块，发送ajax请求
        return request({
            url: '/admin/integralGrade/save',
            method: 'post',
            data: integralGrade
        })
    }
}
```

⭐api会被views所调用

##### views

src之下首先找到views文件夹，需要调用api且显示数据

即下图画圈部分所要展示的东西

![image-20220127113135651](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220127113135651.png)



此类文件的基本结构如下

```vue
<template>
    <div>
    </div>
</template>



<script>
export default {
    data () {
        return {
            
        }
    },
    created () {
        
    },

    methods: {
        
    }

}
</script>

<style scoped>

</style>




```



如列表展示功能——需要获取数据，显示数据

获取数据即写方法调用API，

显示数据即写html标签调用模板



##### router

router文件夹，其中有一个index.js文件

其中内容管理的是侧边栏，以及侧边栏标签与内容的绑定

如下的配置

```js
 {
    path: '/admin/integral-grade',
    component: Layout,
    redirect: '/admin/integral-grade/list',
    name: 'adminIntegralGrade',
    meta: { title: '积分等级', icon: 'el-icon-s-grid' },
    alwaysShow: true,//如果父节点下只有一个子节点的时候就可以选择是否显示父节点
    children: [
      {
        path: 'list',
        name: 'adminIntegralGradeList',
        component: () => import('@/views/admin/integral-grade/list'),
        meta: { title: '积分等级列表', icon: '' }
      },
      {
        path: 'create',
        name: 'adminIntegralGradeCreate',
        component: () => import('@/views/admin/integral-grade/form'),
        meta: { title: '新增积分等级', icon: '' }
      },
      {
        path: 'edit/:id',
        name: 'adminIntegralGradeEdit',
        component: () => import('@/views/admin/integral-grade/form'),
        meta: { title: '修改积分等级', icon: '' },
        hidden: true
      }
    ]
  },
```

实际效果如下

![image-20220129151755166](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220129151755166.png)



调用关系大致如下



![image-20220129143804911](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220129143804911.png)





### 目录进一步分析

#### 页面入口

public/index.html

所有的页面都是依靠这个页面，不同的显示效果则是依靠js动态渲染

#### 引用

src/main.js

这个文件主要是为了引入一些引用文件

【类似于java类中的import】

以及创建唯一的vue对象且绑定id为app的标签

```vue
new Vue({
  el: '#app',
  router,//引入路由模块
  store,
  render: h => h(App)//引用App所指的文件
})
```

#### 路由

在上面所指的App是同目录下的App.vue文件

因为App.vue文件中以App的名字导出了模块

模块中为router-view，即路由出口



路由出口需要与路径绑定

则在src/router文件夹下的文件中，定义了路径与页面展示的绑定

src/views文件夹下即为页面展示



#### layout

前端工程——个人理解就是将一个页面拆碎，以组件的形式一个个再动态的拼接

当我们访问list表单的时候

页面总共由三部分组成

![image-20220129170138432](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220129170138432.png)



而在工程中的呈现即App调用Layout，layout再去调用自己的三个部件，之下的三个部件再去调用各自的所属部件，最终渲染出页面，即树形结构



![image-20220129170046447](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220129170046447.png)









## 普通列表的CRUD

查询的表单显示在页面A，修改和新增共用同一个页面B

删除和修改按钮显示在A中

则需要编写两个页面【form表单和list展示】

配置路由如下，三个路由，其中修改路由为隐藏路由

```js
  {
    path: '/admin/integral-grade',
    component: Layout,
    redirect: '/admin/integral-grade/list',
    name: 'adminIntegralGrade',
    meta: { title: '积分等级', icon: 'el-icon-s-grid' },
    alwaysShow: true,//如果父节点下只有一个子节点的时候就可以选择是否显示父节点
    children: [
      {
        path: 'list',
        name: 'adminIntegralGradeList',
        component: () => import('@/views/admin/integral-grade/list'),
        meta: { title: '积分等级列表', icon: '' }
      },
      {
        path: 'create',
        name: 'adminIntegralGradeCreate',
        component: () => import('@/views/admin/integral-grade/form'),
        meta: { title: '新增积分等级', icon: '' }
      },
      {
        path: 'edit/:id',
        name: 'adminIntegralGradeEdit',
        component: () => import('@/views/admin/integral-grade/form'),
        meta: { title: '修改积分等级', icon: '' },
        hidden: true
      }
    ]
  }
```





基本步骤：

​	1、写api

​	2、写axios请求

​	3、写html显示数据

api文件的基本框架

```java
// 引入axios的初始化模块
import request from '@/utils/request'


export default{
    // 定义模块成员
}
```

axios请求和数据显示是在同一个view文件，框架为

```vue
<template>
    <div class="app-container">
    
    </div>
</template>

<script>

export default {
    data () {
        return {
        }
    },
    created () {
    },
    methods: {

    }
}
</script>

<style scoped>

</style>
```



### 查询并展示所有

#### 1、api的编写

```js
	list(){
        // 调用axios初始化模块，发送ajax请求
        return request({
            url: '/admin/integralGrade/list',
            method: 'get'
        })
    }
```

#### 2、调用api发送axios请求

先导入api模块

```vue
import integralGradeApi from '@/api/admin/integral-grade'
```

需要定义的东西包含：定义参数、定义函数、确定在什么阶段来调用此函数

⭐分析思路

因为是展示所有列表，因此返回值为列表，定义为list

函数无形参，只需要执行api方法即可，查询成功后将response的data传到list中

在视图渲染前调用此函数【created】

```vue
export default {
    data () {
        return {
            // 定义list
            list: []
        }
    },
    created () {
        this.fetchData()
    },

    methods: {
        fetchData(){
            integralGradeApi.list().then(response=>{
                this.list=response.data.list
            })
        }
    }
}
```

#### 3、写html显示数据



```html
		<el-table :data="list" border stripe>
            <el-table-column type="index" width="50"/>
            <el-table-column prop="borrowAmount" label="借款额度"/>
            <el-table-column prop="integralStart" label="区间积分最小值"/>
            <el-table-column prop="integralEnd" label="区间积分最大值"/>
        </el-table>
```

运用element框架，prop绑定各项数据的name即可



### 删除单条信息

#### 	1、写api

```js
// 根据id删除积分区间
    deleteById(id){
        // 调用axios初始化模块，发送ajax请求
        return request({
            url: '/admin/integralGrade/delete/'+id,
            method: 'delete'
        })
    }
```



#### 	2、写axios请求

```vue
        deleteById(id){

            this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
                }).then(() => {
                    integralGradeApi.deleteById(id).then(response=>{
                        this.$message({
                        showClose: true,
                        message: response.message,
                        type: 'success'
                    })
                    this.fetchData()
                    }).catch(err=>{
                        this.$message({
                            showClose: true,
                            message: response.message,
                            type: 'error'
                            })
                        })
                }).catch(() => {
                    this.$message({
                    type: 'info',
                    message: '已取消删除'
                    });          
                });
            // console.log(id);
            
        }
```



删除操作需要用户多一步确认操作，确定后再执行删除操作，删除操作也分成功与失败，再进行判断成功失败如何响应





#### 	3、写html显示数据

```html
        <el-table :data="list" border stripe>
            <el-table-column type="index" width="50"/>
            <el-table-column prop="borrowAmount" label="借款额度"/>
            <el-table-column prop="integralStart" label="区间积分最小值"/>
            <el-table-column prop="integralEnd" label="区间积分最大值"/>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button type="danger" size="mini" icon="el-icon-delete"
                        @click="deleteById(scope.row.id)"
                    >
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
```

只需要加一个删除按钮，按钮需要传入一个id参数





### 新增信息

#### 	1、写api

```js
// 新增积分区间
    save(integralGrade){
        // 调用axios初始化模块，发送ajax请求
        return request({
            url: '/admin/integralGrade/save',
            method: 'post',
            data: integralGrade
        })
    }
```



#### 	2、写axios请求

初始化一个对象【新增的数据】——用于接受存储对象

初始化一个布尔类型的变量——初始化为false，当save方法被调用后会被设为true，用于禁用保存按钮，防止用户多次点击按钮导致数据有无效新增



方法——调用save的api，调用完之后就返回列表展示页面

在保存按钮点击时调用save方法

```vue
export default {
    data () {
        return {
            integralGrade:{},// 初始化数据
            saveBtnDisabled: false // 保存按钮是否禁用，防止表单重复提交
        }
    },
    created () {
    },
    methods: {
        save(){
            integralGradeApi.save(this.integralGrade).then(response=>{
                this.$message.success(response.message)
                // 表单提交完后自动跳转到列表展示页面
                this.$router.push('/admin/integral-grade/list')
            })
        }
    }
}
```



#### 	3、写html显示数据

```html
        <el-form label-width="120px">
            <el-form-item label="借款额度">
                <el-input-number v-model="integralGrade.borrowAmount" :min="0"/>
            </el-form-item>
            <el-form-item label="积分区间最小值">
                <el-input-number v-model="integralGrade.integralStart" :min="0"/>
            </el-form-item>
            <el-form-item label="积分区间最大值">
                <el-input-number v-model="integralGrade.integralEnd" :min="0"/>
            </el-form-item>
            <el-form-item>
                <el-button 
                    :disabled="saveBtnDisabled"
                    type="primary"
                    @click="save()"
                >
                    保存
                </el-button>
            </el-form-item>
        </el-form>
```





### 修改信息

#### 	1、写api

```js
// 回显数据所用的根据id查询积分区间
    getById(id){
        // 调用axios初始化模块，发送ajax请求
        return request({
            url: '/admin/integralGrade/get/'+id,
            method: 'get'
        })
    },

    // 根据id修改积分区间内容
    updateById(integralGrade){
        return request({
            url: '/admin/integralGrade/update',
            method: 'put',
            data: integralGrade
        })
    }
```



#### 	2、写axios请求

修改也需要初始化一个对象，不过由于修改和新增使用同一个页面，因此可省略

修改操作可以分解为——前端点击修改按钮触发修改路由，页面跳转到修改页面，页面数据回显，修改保存，返回列表页面

我们设置的修改路由中可以穿一个id参数，跳转到form页面时可根据这个id的参数是否为空来判断是修改还是新增

因此可将update和save方法各自写好后，用一个逻辑判断函数来判断调用save还是update

```vue
export default {
    data () {
        return {
            integralGrade:{},// 初始化数据
            saveBtnDisabled: false // 保存按钮是否禁用，防止表单重复提交
        
        }
    },
    created () {
        // 路由router中存在id的时候调用这个函数
        if(this.$route.params.id){
            this.fetchById(this.$route.params.id)
        }
    },
    methods: {

        fetchById(id){
            integralGradeApi.getById(id).then(response=>{
                this.integralGrade= response.data.record
            })
        },

        saveOrUpdate(){
            // 保存按钮被按了一次之后就禁用，防止数据误增
            this.saveBtnDisabled=true
            
            // 若实体中的id不为空，则调用修改函数
            if(this.integralGrade.id){
                this.update()
            }else{
                // 若为空则调用新增函数
                this.save()
            }

        },


        save(){
            integralGradeApi.save(this.integralGrade).then(response=>{
                this.$message.success(response.message)
                // 表单提交完后自动跳转到列表展示页面
                this.$router.push('/admin/integral-grade/list')
            })
        },
        update(){
            integralGradeApi.updateById(this.integralGrade).then(response=>{
                this.$message.success(response.message)
                // 表单提交完后自动跳转到列表展示页面
                this.$router.push('/admin/integral-grade/list')
            })    
        }



    }
}
```



#### 	3、写html显示数据

html只需要添加展示页面的修改按钮

这个按钮需要兼具路由跳转的功能，还需要传入id值

scope用于传id，router-link标签用于设置路由目的地

```html
<el-table :data="list" border stripe>
            <el-table-column type="index" width="50"/>
            <el-table-column prop="borrowAmount" label="借款额度"/>
            <el-table-column prop="integralStart" label="区间积分最小值"/>
            <el-table-column prop="integralEnd" label="区间积分最大值"/>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <router-link :to="'/admin/integral-grade/edit/'+scope.row.id"
                        style="margin-right:5px"
                    >
                        <el-button type="primary" size="mini" icon="el-icon-edit"
                            @click="updateById(scope.row.id)"
                        >
                            修改
                        </el-button>
                    </router-link>
                        
                    
                    <el-button type="danger" size="mini" icon="el-icon-delete"
                        @click="deleteById(scope.row.id)"
                    >
                        删除
                    </el-button>
                    
                </template>
            </el-table-column>
        </el-table>
```



## 带搜索与分页的CRUD

![image-20220208230353489](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220208230353489.png)



### 分页

### 搜索框

### 列表展示











# 组件

## lombok

添加lombok依赖

```java
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```



在实体类的类名上添加@Data注解

可以省去写构造方法与getter、setter方法

## mybatis-plus

添加依赖

```java
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>${mybatis-plus.version}</version>
</dependency>
```

配合springboot

### BaseMapper接口

我们自己创建的mapper接口可以直接继承basemapper接口

再填上实体类的泛型即可

如下

```java
public interface UserMapper extends BaseMapper<User> {

}
```

basemapper接口里提供了基础方法

到时候service可直接调用



### 配置类

搞一个配置类

```java
@Configuration
@MapperScan("com.wzy.loan.core.mapper")
public class MybatisPlusConfig {
}
```

扫描mapper的全包名

### wrapper

wrapper类的继承关系

![image-20220119150658834](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220119150658834.png)



#### queryWrapper

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



### 注解

TableName

实体类映射表名

TableId(value,type)

表主键映射

TableField(value)

表非主键列名

TableLogic

标记逻辑删除字段



## swagger



### 1、导包，新版本是swagger 的springboot starter

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



### 3、在controller中使用

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



### 4、访问swagger

旧版本需要在端口号后加/swagger-ui.html来访问

[如]: http://localhost:8110/swagger-ui.html

新版本则是

[如]: http://localhost:8110/swagger-ui/







## npm

npm init

npm install

npm rum [scriptsName]



## Nginx配置

修改nginx-1.20.2\conf\nginx.conf 文件，在server配置中添加如下三个配置

访问nginx为http://localhost:80 ，nginx会将不同的url请求代理分配给各自的端口号，如url中带admin的是后台管理系统，后端部署的服务端口是8110，则带admin的url访问时候nginx会更改80端口为8110

​		location ~ /core/ {
​            proxy_pass http://localhost:8110;	
​        }
​        location ~ /sms/ {
​            proxy_pass http://localhost:8120;	
​        }
​        location ~ /oss/ {
​            proxy_pass http://localhost:8130;	
​        }

```conf
 server {
        listen       80;
        server_name  localhost;


        location ~ /admin/ {
            proxy_pass http://localhost:8110;	
        }
        location ~ /sms/ {
            proxy_pass http://localhost:8120;	
        }
        location ~ /oss/ {
            proxy_pass http://localhost:8130;	
        }

    location / {
        root   html;
        index  index.html index.htm;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   html;
    }

}
```

启动nginx直接在命令行中start nginx即可



## 数据字典以及easyexel

数据字典——可存多级目录类数据

此处使用二级目录

parentid指的是二级目录项所属的一级目录id

dict_code只有一级目录有

![image-20220130163801955](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220130163801955.png)





## 引入redis

### 应用场景

①在获取一些短期内不会发生改动的数据时可以使用，用于缓解数据库查询的压力，buffer的作用



②暂存一些生命周期较短的数据，如验证码

### 整合springboot

步骤是1、导包	2、配置	3、应用

#### 导包

```xml
<!--        redis依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
<!--        缓存连接池-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
<!--        redis存储json序列化-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
        </dependency>
```

#### 配置

在项目中需要配置连接信息——在yml或properties中

对redis中所存的数据进行序列化处理——配置类

##### yml配置

```xml
spring:
  redis:
    host: 175.178.1.64
    port: 6379
    database: 0
    password: 888888 #默认为空
    timeout: 3000ms #最大等待时间，超时则抛出异常，否则请求一直等待
    lettuce:
      pool:
        max-active: 20  #最大连接数，负值表示没有限制，默认8
        max-wait: -1    #最大阻塞等待时间，负值表示没限制，默认-1
        max-idle: 8     #最大空闲连接，默认8
        min-idle: 0     #最小空闲连接，默认0
```



##### 配置类

可以使用Another Redis Desktop Mnager来尝试连接数据库查看其中的数据

未配置之前数据基本不可读

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory factory){
        RedisTemplate<String, Object> stringObjectRedisTemplate = new RedisTemplate<>();

//        设置连接池工厂
        stringObjectRedisTemplate.setConnectionFactory(factory);

//        key的序列化方式
        StringRedisSerializer serializer = new StringRedisSerializer();
        stringObjectRedisTemplate.setKeySerializer(serializer);



        //以下解决value的序列化方式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        //将当前对象的数据类型也存入序列化的结果字符串中
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        // 解决jackson2无法反序列化LocalDateTime的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        stringObjectRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);



        return stringObjectRedisTemplate;
    }
}
```



#### 应用

此项目的应用场景就是为了减缓数据库的压力，将一些短期内不会发生改动的数据存到redis中便于用户访问

这样的逻辑即——在查询数据的service方法中，先查询redis中是否有某个key，若无则查询数据库，查询成功则将数据以规定的key存入redis并且设定存活时间

⭐重点是如何设计key

##### 1、尝试查询redis，若有则返回数据

```java
try {
    List<Dict> o = (List<Dict>) redisTemplate.opsForValue().get("srb:admin:dictList:" + parentId);
    if(o!=null){
        return o;
    }
} catch (Exception e) {
    log.error("redis异常"+ ExceptionUtils.getStackTrace(e));
}
```

##### 2、查询数据库操作

```java
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
```



##### 3、写入redis后返回数据

```java
//        查询完数据库后将数据存入redis
        try {
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("srb:admin:dictList:"+parentId,dicts,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis异常"+ ExceptionUtils.getStackTrace(e));
        }

        return dicts;
```





## 腾讯云存储





## 短信服务

![image-20220206183741389](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220206183741389.png)





## nuxt

![image-20220205201120003](C:\Users\85251\AppData\Roaming\Typora\typora-user-images\image-20220205201120003.png)



ssr

服务器渲染——如thymeleaf、jsp前端模板

是后端查询数据后将数据加载到页面，再将页面返回给浏览器







# 统一处理



## 统一返回结果

为了防止歧义，response中除了返回数据之外还需要给定一些内容

这个项目所用的是包含code、message和data三种数据的返回类型R

其中code和message用枚举类型构建，data并入到返回类型R中

### 状态码枚举类型实现

```java
public enum ResponseEnum {

    SUCCESS(0,"成功"),
    ERROR(-1,"失败");
    private Integer code;
    private String message;


}

```



根据需求多设定一些code和对应的message

### 返回类型R实现

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



### 统一返回结果的CRUD

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





## 统一异常处理

### 针对不同异常类的处理模板

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



### 自定义异常

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

### 断言assert

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

### 阶段分类异常

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





## 日志处理



### 日志级别

springboot 的yml配置中

```yml
logging:
  level:
    root: error
```

日志级别由低到高为

trace——debug——info——warn——error

还有all和off



### logback框架

需要配置一个xml文件，名为logback-spring

这个名字springboot框架会自动识别

#### property

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

#### 控制台日志

控制台日志输出的配置标签，其中${}中就是输入之前property配的 name

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        <charset>${ENCODING}</charset>
    </encoder>
</appender>
```



#### log文件输出

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

#### 环境配置选择

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

#### 滚动日志记录

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



## 日期格式配置

### 统一配置类

一个配置类通吃所有localDateTime数据类型的格式设置

```java
@Configuration
//localDateTime类型数据的返回数据格式配置， Date类型的格式需要另外配置
public class localDateTimeConfig {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;

    public LocalDateTimeSerializer localDateTimeSerializer(){
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return builder -> builder.serializerByType(LocalDateTime.class,localDateTimeSerializer());
    }

}
```

### 单独注解

在实体类的字段之上配置JsonFormat注解来设置这一个字段的格式

```java
@ApiModelProperty(value = "创建时间")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createTime;
```





# 概念性知识点



## 单点登录

简单的登录验证需要查询数据库，然后记录到cookie中

但微服务情况下，每个端口之间是不相认的，若还是采用之前的验证方法则会造成每次访问不同的微服务都需要各自进行验证

因此引用了token的解决方法，即第一次登录时访问数据库验证，返回给用户一个token字符串，每次访问其他不同的微服务时，http访问头中可携带此token来作为身份验证



token虽然简单，一个字符串应付多个微服务的登录验证，但在进行某些重要操作时还有安全问题，因此可以将短信随机验证码和token结合起来使用来执行一些重要的操作











































