[TOC]



# foodie学习记录

## 1. 为什么不使用数据库外键?

- 性能影响

  检查数据完整性，强一致性，降低效率

- 热更新

  无法完成热更新

- 降低耦合性

  可以在业务逻辑上完成

- 数据分库分表

  有外键关联，难以做分库分表，耦合度太高。数据放在两个数据库中，存在外键关联无法分离。

## 2. Restful Web Service

- 通信方式
- 信息传递
- 无状态
- 独立性

## 3. Rest设计规范

- GET:获取  	 			-> /order/{id}
- POST:添加				-> /order
- PUT:更新					-> /order/{id}
- DELETE:删除			->/order/{id}
- PATCH:部分更新		->/order/{id}

**url中采用名词描述,不使用动词描述**



## 4. 事务的传播-Propagation

REQUIRED:使用当前的事务,如果当前没有事务,则自己新建一个事务,子方法必须运行在一个事务中,如果当前存在事务,则加入这个事务,成为一个整体。

SUPPORTS：如果当前有事务，则使用事务；如果当前没有事务，则不使用事务

MANDATORY：该传播属性强制必须存在一个事务，如果不存在，则抛出异常

REQUIRES_NEW：如果当前有事务，则挂起该事务，并且自己创建一个新的事务给自己使用；如果当前没有事务，则同REQUIRED

NOT_SUPPORTED：以非事务形式执行，如果当前存在事务则挂起

NEVER：如果当前有事务存在，则抛出异常

NESTED：如果当前有事务则开启子事务（嵌套事务），嵌套事务是独立提交的，如果当前没有事务，则同REQUIRED。但是如果父事务提交，则携带子事务一起提交。如果父事务回滚，则子事务会一起回滚。相反，子事务异常，则父事务可以回滚或不回滚。

## 5. 创建分布式全局唯一ID

1.使用UUID，缺点：UUID过长且无序，InnoDB根据主键排序，会造成过大的IO压力。

1.使用自增主键，加UUID唯一索引，把自增主键看成形式上的主键，缺点：自增主键有最大值，会产生溢出

2.每个数据库id起始不同,采用相同步长

3.另一个数据库中存储当前id，获取全局唯一的自增序列号或者各表的MaxID（如Redis)，缺点：引入新的组件，复杂度上升。同时存在单点问题，可用性降低。

4.Twitter的SnowFlake和美团  的Leaf

## 6. 工具类

### ① Restful结果类

JSONResult.java

```java
package com.xybh.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @Title: JSONResult.java
 * @Description: 自定义响应数据结构
 * 				本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * 				前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 *
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 * 				556: 用户qq校验异常
 */
public class JSONResult {

    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应中的数据
     */
    private Object data;

    @JsonIgnore
    private String ok;

    public static JSONResult build(Integer status, String msg, Object data) {
        return new JSONResult(status, msg, data);
    }

    public static JSONResult build(Integer status, String msg, Object data, String ok) {
        return new JSONResult(status, msg, data, ok);
    }

    public static JSONResult ok(Object data) {
        return new JSONResult(data);
    }

    public static JSONResult ok() {
        return new JSONResult(null);
    }

    public static JSONResult errorMsg(String msg) {
        return new JSONResult(500, msg, null);
    }

    public static JSONResult errorMap(Object data) {
        return new JSONResult(501, "error", data);
    }

    public static JSONResult errorTokenMsg(String msg) {
        return new JSONResult(502, msg, null);
    }

    public static JSONResult errorException(String msg) {
        return new JSONResult(555, msg, null);
    }

    public static JSONResult errorUserQQ(String msg) {
        return new JSONResult(556, msg, null);
    }

    public JSONResult() {

    }

    public JSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JSONResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public JSONResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

}

```

### ② 脱敏工具类

```java
/**
 * 通用脱敏工具类
 * 可用于：
 *      用户名
 *      手机号
 *      邮箱
 *      地址等
 */
public class DesensitizationUtil {

    private static final int SIZE = 6;
    private static final String SYMBOL = "*";

    public static void main(String[] args) {
        String name = commonDisplay("慕课网");
        String mobile = commonDisplay("13900000000");
        String mail = commonDisplay("admin@imooc.com");
        String address = commonDisplay("北京大运河东路888号");

        System.out.println(name);
        System.out.println(mobile);
        System.out.println(mail);
        System.out.println(address);
    }

    /**
     * 通用脱敏方法
     * @param value
     * @return
     */
    public static String commonDisplay(String value) {
        if (null == value || "".equals(value)) {
            return value;
        }
        int len = value.length();
        int pamaone = len / 2;
        int pamatwo = pamaone - 1;
        int pamathree = len % 2;
        StringBuilder stringBuilder = new StringBuilder();
        if (len <= 2) {
            if (pamathree == 1) {
                return SYMBOL;
            }
            stringBuilder.append(SYMBOL);
            stringBuilder.append(value.charAt(len - 1));
        } else {
            if (pamatwo <= 0) {
                stringBuilder.append(value.substring(0, 1));
                stringBuilder.append(SYMBOL);
                stringBuilder.append(value.substring(len - 1, len));

            } else if (pamatwo >= SIZE / 2 && SIZE + 1 != len) {
                int pamafive = (len - SIZE) / 2;
                stringBuilder.append(value.substring(0, pamafive));
                for (int i = 0; i < SIZE; i++) {
                    stringBuilder.append(SYMBOL);
                }
                if ((pamathree == 0 && SIZE / 2 == 0) || (pamathree != 0 && SIZE % 2 != 0)) {
                    stringBuilder.append(value.substring(len - pamafive, len));
                } else {
                    stringBuilder.append(value.substring(len - (pamafive + 1), len));
                }
            } else {
                int pamafour = len - 2;
                stringBuilder.append(value.substring(0, 1));
                for (int i = 0; i < pamafour; i++) {
                    stringBuilder.append(SYMBOL);
                }
                stringBuilder.append(value.substring(len - 1, len));
            }
        }
        return stringBuilder.toString();
    }

}
```

### ③手机号/邮箱校验工具类

```java
public class MobileEmailUtils {

    public static boolean checkMobileIsOk(String mobile) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        boolean isMatch = m.matches();
        return isMatch;
    }

    public static boolean checkEmailIsOk(String email) {
        boolean isMatch = true;
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            isMatch = false;
        }
        return isMatch;
    }
}
```



## 7. 配置文件

### 1. Swagger2配置类

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    // swagger2访问地址: 主机名:端口号/swagger-ui.html
    //  bootstrap页面: 主机名:端口号/doc.html

    /**
     * 配置swagger2核心内容 docket
     * @return
     */
    @Bean
    public Docket createRestApi() {
                    //指定api类型为SWAGGER2
        return new Docket(DocumentationType.SWAGGER_2)
                // 用于定义api文档的汇总信息
                .apiInfo(apiInfo())
                // 指定Controller的包名
                .select()
                .apis(RequestHandlerSelectors
                       .basePackage("com.xybh.controller"))
                //包名底下所有controller
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档页标题
                .title("吃货商城后端API文档")
                // 文档描述
                .description("专为吃货商城编写的后端API文档")
                // 联系人
                .contact(new Contact("xybh", null, null))
                // 版本信息
                .version("v0.0.1")
                // 项目URL
                .termsOfServiceUrl("127.0.0.1")
                .build();
    }
}
```

### 2. log4j日志配置文件

```yml
log4j.rootLogger=DEBUG,stdout,file
log4j.additivity.org.apache=true

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.threshold=INFO
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%-5p %c{1}:%L - %m%n

# 每天保存日志文件
log4j.appender.file=org.apache.log4j.DailyRollingFileAppender
# 输出布局
log4j.appender.file.layout=org.apache.log4j.PatternLayout
# 文件日期参数
log4j.appender.file.DatePattern='.'yyyy-MM-dd-HH-mm
# 输出格式
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
# 输出信息级别(INFO)
log4j.appender.file.Threshold=INFO
# 追加输出
log4j.appender.file.append=true
# 文件保存地址
log4j.appender.file.File=/workspaces/logs/foodie-api/imooc.log
```



## 8. Cookie与Session

### Cookie

- 以键值对的形式存储信息在服务器
- cookie不能跨域，当前及其父级域名可以取值
- cookie可以设置有效期
- cookie可以设置path

### Session

- 基于服务器内存的缓存(非持久化)，可保存请求会话
- 每个session通过sessionid来区别不同请求
- session可设置过期时间

## 9. AOP通知:

1. 前置通知: 在方法调用之前仔细
2. 后置通知: 在方法正常调用之后执行
3. 环绕通知: 在方法调用之前和之后,都分别可以执行的通知
4. 异常通知: 如果在憨憨调用过程中发生异常,则通知
5. 最终通知: 在方法调用之后执行

```
切面表达式:
* execution 代表所要执行的表达式主体
* 第一处 * 表示方法返回类型
* 第二次 包名表示aop监控的包名
* 第三处 .. 代表该包以及子包下的所有类方法
* 第四次 *  代表所有类
* 第五处 *(..) *代表方法 (..)代表参数
execution(* com.xybh.service.impl..*.*(..) )
```

## 10. 分页插件 PageHelper

```xml
<!-- pageHelper分页插件 -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.3.0</version>
</dependency>
```

```yml
# 分页插件配置
pagehelper:
  helper-dialect: mysql #数据库方言
  support-methods-arguments: true	#是否支持分页的参数传参
```

##  11. MyBaits SQL编写

```xml &lt;choose&gt;
<!-- 多条件判断 -->
<!-- ''需要转义(&quot) -->
<choose>
    <when test="paramsMap.sort == &quot;c&quot;">
        i.sell_counts desc
    </when>
    <when test="paramsMap.sort ==  &quot;p &quot;">
        tempSpec.price_discount asc
    </when>
    <otherwise>i.item_name asc</otherwise>
</choose>
<!--
    k: 默认, 代表默认排序,根据name
    c: 根据销量排序
    p: 根据价格排序
-->

<!-- 遍历List -->
<foreach collection="paramsList" index="0" item="specId" open="(" separator="," close=")" >
            #{specId}
</foreach>

<!-- 
	collection 	List名称
	index		起始下标
	item		遍历变量名称
	open		左侧符号,(
	close		右侧符号,)
	separator	分隔符,,
-->
```

## 12.购物车存储信息

### Cookie

- 无须登录、无须查库、保存在浏览器端
- 优点：性能好、访问快，没有和数据库交互
- 缺点1：换电脑购物车数据会丢失
- 缺点2：电脑被其他人登录、隐私安全

### Session

- 用户登录后，购物车数据放入用户会话
- 优点：初期性能较好，访问快
- 缺点1：session基于内存，用户量庞大影响服务器性能
- 缺点2：只能存在于当前会话，不适用集群与分布式系统

### 数据库

- 用户登录后，购物车数据存入数据库
- 优点：数据持久化，可在任何地点任何时间访问
- 缺点：频繁读写数据库，造成数据库压力

### Redis

- 用户登录后，购物车数据存入Redis
- 优点1：数据持久化，可在任何地点任何时间访问
- 优点2：频繁读写只基于缓存，不会造成数据库压力
- 优点3：适用于集群与分布式系统，可扩展性强

**未登录使用Cookie，已登录使用Redis**

## 13.防止超卖

- synchronized:不推荐使用,集群下无用,性能低下
- 锁数据库:不推荐,导致数据库性能低下
- 分布式锁:zookeeper redis

单体: 乐观锁

```xml
<update id="decreaseItemSpecStock">
        UPDATE
            items_spec
        SET stock = stock - #{pendingCounts}
        WHERE id = #{specId}
          AND stock >= #{pendingCounts}
        #判断当前库存大于需要扣除的数量
</update>
```

## 14.发送Restful请求

```java
@Configuration
public class WebMvcConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}

@AutoWired
private RestTemplate restTemplate;

HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("userId", "imooc");
        header.add("password", "imooc");

HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, header);

ResponseEntity<JSONResult> responseEntity =
                restTemplate.postForEntity(PAYMENT_URL,
                        entity,
                        JSONResult.class);
JSONResult paymentResult = responseEntity.getBody();
```



## 15.定时任务

```java
/**
 * 使用定时任务关闭超时未支付订单,会存在弊端:
 * 1. 会有时间差，程序不严谨
 *      10:39下单, 11:00不足1小时, 12:00检查, 超过1小时多39分钟
 * 2. 不支持集群
 *      单机没问题,使用集群后会有多个定时任务
 *      解决方案: 只使用一台计算机节点,单独用来运行所有的定时任务
 * 3. 会对数据库全表搜索，及其影响数据库性能
 *      select * from order where orderStatus = 10;
 * 定时任务,仅仅适用于小型轻量级项目,传统项目
 * 
 * 消息队列:MQ -> RabbitMQ, RocketMQ, Kafka, ZeroMQ...
 */
    @Scheduled(cron = "0 0 0/1 * * *")
    public void autoCloseOrder() {
        orderService.closeOrder();
        System.out.println("执行定时任务,当前时间为:"+ DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
}
// 启动类上加上 @EnableScheduling
// corn表达式生成网站: https://qqe2.com/cron
```

## 16.图片上传

```java
 @ApiOperation(value = "修改用户头像", notes = "修改用户头像", httpMethod = "PUT")
    @PutMapping("uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {

        // 定义头像保存的地址
        String fileSpace = IMAGE_USER_FACE_URL;
        // 在路径上为每个用户增加一个userId, 用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        // 开始文件上传
        if (file != null) {
            // 获取文件上传的文件名称
            String filename = file.getOriginalFilename();
            if (StringUtils.isNotBlank(filename)) {
                // 文件重命名  imooc-face.png -> ["imooc-face", "png"]
                String[] fileNameArray = filename.split("\\.");

                // 获取文件的后缀名
                String suffix = fileNameArray[fileNameArray.length - 1];

                // face-{userid}.png
                // 文件名重组  覆盖式上传, 增量式: 额外拼接当前时间
                String newFileName = "face-" + userId + "-" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + "." + suffix;

                // 文件上传的最终保存位置
                String finalFacePath = fileSpace + File.separator + uploadPathPrefix + File.separator + newFileName;
                FileOutputStream fileOutputStream = null;
                InputStream inputStream = null;
                try {
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        // 创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            return JSONResult.errorMsg("文件不能为空!");
        }
        // TODO 后续要改,增加令牌token,整合进redis,分布式会话
        return JSONResult.ok();
    }
```

## 17.静态资源映射

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     *  实现静态资源的映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                // 映射swagger2
                .addResourceLocations("classpath:/META-INF/resources/")
                // 映射本地静态资源
                .addResourceLocations("file:E:/Tomcat/apache-tomcat-9.0.37/webapps/foodie-center/");
    }
}
```



