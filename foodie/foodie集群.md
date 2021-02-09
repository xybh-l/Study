foodie集群学习

## 一、什么是Nginx

- **Nginx(engine x)** 是一个高性能的HTTP和反向代理web服务器，同时也提供IMAP/POP3/SMTP服务。
- 主要功能：反向代理
- 通过配置文件可以实现集群和负载均衡
- 静态资源虚拟化

## 二、常见的服务器

- MS IIS							 asp.net
- Weblogic、Jboss 		   传统行业 ERP/物流/电信/金融
- Tomcat、Jetty			    J2EE
- Apache、Nginx			  静态服务，反向代理
- Netty							   高性能服务器编程			



## 三、代理

### 1. 什么是正向代理

- 客户端请求目标服务器之间的一个代理服务器
- 请求会先经过代理服务器，然后再转发请求到目标服务器，获得内容后最后响应给客户端

### 2. 什么是反向代理

- 用户请求目标服务器，由目标服务器决定访问哪个ip

## 四、Nginx

### 1. Nginx的进程模型

- master进程:主进程

- worker进程:工作进程

  信号:

  nginx -s stop

  nginx -s quit

  nginx -s reload

  nginx -t

### 2.配置文件参数

#### 2.1 main

- user : 工作进程所属用户
- worker_processes:工作进程数量，通常为n-1
- error_log: 保存日志 命令格式 error_log 日志文件名 (debug info notice warn error crit)
- pid: Nginx 启动进程号

#### 2.2 events

- use epoll; 默认使用epoll, worker进程工作模式
- worker_connections 10240; 每个worker允许连接的客户端最大连接数

#### 2.3 http

- include				 	导入外部文件
- default_type 	   	文档默认类型
- log_format 		  	日志格式
- access_log				日志文件保存位置
- sendfile 					发送文件
- keeplive_timeout	保持连接
- gzip							gzip压缩

#### 2.4 server

- listen 						监听端口
- server_name			域名
- location					路由
- error_page				错误状态(跳转页面)

### 3. Nginx常用命令

- 启动 				    nginx
- 快速停止 	        nginx -s stop
- 停止服务器         nginx -s quit
- 重载 		            nginx -s reload
- 测试配置文件     nginx -t
- 查看版本             nginx -v	
- 查看详情             nginx -V
- 帮助文档             nginx -?
- 设置配置文件     nginx -c filename

### 4、Nginx 日志

#### 4.1 Nginx 日志切割(手动)

cut_my_log.sh

```bash
#!/bin/bash
LOG_PATH="/www/server/nginx/logs"
RECORD_TIME=$(date -d "yesterday" +%Y-%m-%d+%H:%M)
PID=/www/server/nginx/logs/nginx.pid
mv ${LOG_PATH}/access.log ${LOG_PATH}/access.${RECORD_TIME}.log
mv ${LOG_PATH}/error.log ${LOG_PATH}/error.${RECORD_TIME}.log
#向Nginx主进程发送信号，用于重新打开日志文件
kill -USR1 `cat $PID`
```



#### 4.2 Nginx 日志切割(自动)

1. 安装定时任务

   ```shell
   yum install crontabs
   ```

2. corntal -e 编辑并且添加一行新的任务

   ```bash
   */1 * * * * /www/server/nginx/sbin/cut_my_log.sh
   ```

3. 重启定时任务

   ```shell
   service crond restart
   ```

附:**常见定时任务命令**

```shell
service crond start			//启动服务
service crond stop			//关闭服务
service crond restart		//重启服务
service crond reload		//重载配置
crontab -e					//编辑任务
crontab	-l					//查看任务列表
```

**定时任务表达式**

|          |  分  |  时  |  日  |  月  | 星期几 |      年(可选)      |
| :------: | :--: | :--: | :--: | :--: | :----: | :----------------: |
| 取值范围 | 0-59 | 0-23 | 1-31 | 1-12 |  1-7   | 2019/2020/2021/... |

   **常见表达式**

- 每分钟执行:

  ```bash
  */1 * * * *
  ```

- 每日凌晨（每天晚上23:59）执行:

  ```bash
  59 23 * * *
  ```

- 每日凌晨1点执行:

  ```bash
  0 1 * * *
  ```


### 5、Nginx静态资源映射

```
server{
	listen			90;
	server_name		localhost;
	
	location / {
		root   /home/foodie-shop;
		index   index.html;
	}
	
	location /static {
		alias  /home/imooc;	
	}
}
```

### 6、Nginx gzip压缩

- gzip on;  					 # 开启gzip压缩，目的：提高压缩效率，节约带宽
- gzip_min_length 1;	# 限制最小压缩，小于1字节文件不会压缩
- gzip_comp_level 3;	# 定义压缩的级别 (压缩比，文件越大，压缩越多，但是cpu使用会越多)
- gzip_type					 # 定义压缩文件的类型

### 7、Nginx 跨域配置

```python
location / { 			
# 允许跨域请求的域, *代表所有
add_header	'Access-Control-Allow-Origin' *;
# 允许带上cookie请求
add_header 'Access-Control-Allow-Credentials' 'true';
# 允许请求的方法, 比如 GET/POST/PUT/DELETE
add_header 'Access-Control-Allow-Methods' *;
# 允许请求的header
add_header 'Access-Control-Allow-Headers' *;
} 
```

### 8、Nginx 防盗链

```python
# 对源站点验证
valid_referers *.imooc.com;
# 非法引入会进入下方判断
if ($invalid_referer) {
    return 404;
}
```

### 9、Nginx 集群

```python
upstream tomcats{
    server 192.168.1.173:8080;  # weight 权重设置 example: weight=1
	server 192.168.1.174:8080;
	server 192.168.1.175:8080;   
}
server{
    listen 	80;
    server_name www.tomcats.com;
    	
    location /{
        proxy_pass http://tomcats;
    }
}
```

负载均衡方式:

<table style="text-align:center;">
	<caption>负载均衡策略</caption>
    <tr>
        <td>轮询</td>
        <td>默认方式</td>
    </tr>
    <tr>
        <td>weight</td>
        <td>权重方式</td>
    </tr>
    <tr>
        <td>ip_hash</td>
        <td>依据ip分配方式</td>
    </tr>
    <tr>
        <td>least_conn</td>
        <td>最少连接方式</td>
    </tr>
    <tr>
        <td>fair</td>
        <td>响应时间方式</td>
    </tr>
    <tr>
        <td>url_hash</td>
        <td>依据URL分配方式</td>
    </tr>
</table>

#### 9.1 轮询

　　最基本的配置方法，上面的例子就是轮询的方式，它是upstream模块默认的负载均衡默认策略。每个请求会按时间顺序逐一分配到不同的后端服务器。

　　有如下参数：

|     参数     |             说明             |
| :----------: | :--------------------------: |
| fail_timeout | 超时时间,与max_fails结合使用 |
|  max_fails   |         最大失败次数         |
|  fail_time   |           停机时间           |
|    backup    |       标记为备用服务器       |
|     down     |         标记为已停机         |

　　**注意：**

- 在轮询中，如果服务器down掉了，会自动剔除该服务器。
- 缺省配置就是轮询策略。
- 此策略适合服务器配置相当，无状态且短平快的服务使用。

#### 9.2 weight

权重方式，在轮询策略的基础上指定轮询的几率。例子如下：

```
    #动态服务器组
    upstream dynamic_zuoyu {
        server localhost:8080   weight=2;  #tomcat 7.0
        server localhost:8081;  #tomcat 8.0
        server localhost:8082   backup;  #tomcat 8.5
        server localhost:8083   max_fails=3 fail_timeout=20s;  #tomcat 9.0
    }
```

　　在该例子中，weight参数用于指定轮询几率，weight的默认值为1,；weight的数值与访问比率成正比，比如Tomcat 7.0被访问的几率为其他服务器的两倍。

　　**注意：**

- 权重越高分配到需要处理的请求越多。
- 此策略可以与least_conn和ip_hash结合使用。
- 此策略比较适合服务器的硬件配置差别比较大的情况。

#### 9.3 ip_hash

　　指定负载均衡器按照基于客户端IP的分配方式，这个方法确保了相同的客户端的请求一直发送到相同的服务器，以保证session会话。这样每个访客都固定访问一个后端服务器，可以解决session不能跨服务器的问题。

```
#动态服务器组
    upstream dynamic_zuoyu {
        ip_hash;    #保证每个访客固定访问一个后端服务器
        server localhost:8080   weight=2;  #tomcat 7.0
        server localhost:8081;  #tomcat 8.0
        server localhost:8082;  #tomcat 8.5
        server localhost:8083   max_fails=3 fail_timeout=20s;  #tomcat 9.0
    }
```

　　**注意：**

- 在nginx版本1.3.1之前，不能在ip_hash中使用权重（weight）。
- ip_hash不能与backup同时使用。
- 此策略适合有状态服务，比如session。
- 当有服务器需要剔除，必须手动down掉。

### 10、配置SSL证书

1. 安装SSL模块

   要在nginx中配置https，就要先安装ssl模块，即`http_ssl_module`.

   - 进入nginx安装目录

   - 新增ssl模块

     ```bash
     ./configure \
     --prefix=/usr/local/nginx \
     --pid-path=/var/run/nginx/nginx.pid \
     --lock-path=/var/lock/nginx.lock \
     --error-log-path=/var/log/nginx/error.log \
     --http-log-path=/var/log/nginx/access.log \
     --with-http_gzip_static_module \
     --http-client-body-temp-path=/var/temp/nginx/client \
     --http-proxy-temp-path=/var/temp/nginx/proxy \
     --http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
     --http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
     --http-scgi-temp-path=/var/temp/nginx/scgi \
     --with-http_ssl_module
     ```

   - 编译和安装

     ```bash
     make
     make install
     ```

2. 配置HTTPS

   - 把ssl证书`*.crt`和`*.key`拷贝到`nginx.conf`同级目录中

   - 新增server监听443端口:

     ```bash
     server {
         listen 443;
         # DNS解析的域名
         server_name www.imoocdsp.com;
         # 开启ssl
         ssl on;
         # 配置ssl证书(修改为自己的crt)
         ssl_certificate *.xybh.crt;
         # 配置证书秘钥(修改为自己的key)
         ssl_certificate_key *.xybh.key;
         # ssl会话cache
         ssl_session_cache shared:SSL:1m;
         # ssl会话超时时间
         ssl_session_timeout 5m;
         # 配置加密套件，写法遵循 openssl 标准
         ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
         ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
         ssl_prefer_server_ciphers on;
         
         location / {
             # 反代地址
             proxy_pass http://tomcats/;
         }
     }
     ```

## 五、分布式架构

### 1、优缺点

1. 优点：
   - 业务解耦
   - 系统模块化，可重用化
   - 提升系统并发量
   - 优化运维部署效率
2. 缺点
   - 架构复杂
   - 部署多个子系统复杂
   - 系统之间通信耗时
   - 融入团队慢
   - 调试复杂

### 2、设计原则

- 异步解耦
- 幂等一致性
- 拆分原则
- 融合分布式中间件
- 容错高可用

### 3、分布式缓存

- 提升读取速度性能(2-8原则)
- 分布式计算领域
- 为数据库降低查询压力
- 跨服务器缓存
- 内存式缓存

## 六、Redis

### 1、什么是Redis

- NoSql数据库
- 分布式缓存中间件
- key-value存储
- 提供海量数据存储访问
- 数据存储在内存里,读取更快

### 2、缓存方案对比

<table>
    <tr style="text-align:center; font-weight:bold;">
        <td>缓存方案</td>
    	<td>优点</td>
    	<td>缺点</td>
    </tr>
	<tr>
        <td>Ehcache</td>
        <td>
            <ol>
                <li>基于Java开发</li>
                <li>基于JVM缓存</li>
                <li>简单、轻巧、方便</li>
            </ol>
        </td>
        <td>
            <ol>
                <li>集群不支持(缓存不共享)</li>
                <li>分布式不支持</li>
            </ol>
        </td>
	</tr>
	<tr>
        <td>Memcache</td>
        <td>
            <ol>
                <li>简单的key-value存储(单一String类型)</li>
                <li>内存使用率比较高</li>
                <li>多核处理,多线程</li>
            </ol>
        </td>
        <td>
            <ol>
                <li>无法容灾</li>
                <li>无法持久化</li>
            </ol>
        </td>
	</tr>
    <tr>
        <td>Redis</td>
        <td>
            <ol>
                <li>丰富的数据结构</li>
                <li>持久化</li>
                <li>主从同步、故障转移</li>
                <li>内存数据库</li>
            </ol>
        </td>
        <td>
            <ol>
                <li>单线程(6.0加入多线程)</li>
                <li>单核</li>
            </ol>
        </td>
	</tr>
</table>

### 3、Redis的数据类型

#### 3.1 string

string:最简单的字符串类型键值对缓存。

**key相关**

keys * : 查看所有的key(不建议在生产环境上使用，有性能影响)

type key: key的类型

**string类型**

- get/set/del:查询/设置/删除
- set key value: 设置key(已存在,则覆盖)
- setnx key value: 设置key(已存在,不覆盖)
- set key value ex time: 设置带过期时间的数据
- expire key: 设置过期时间
- ttl: 查看过期时间, -1-永不过期， -2已过期
- append key: 追加字符串
- strlen key:字符串长度
- incr key: 数字值累加一
- decr key:数字值累减一
- incrby key num: 累加给定数值
- decrby key num: 累减给定数值
- getrange key start end: 截取数据, end=-1 代表最后
- setrange key start newdata: 从start位置开始替换数据
- mset: 连续设值
- mget: 连续取值
- msetnx: 连续设值不覆盖



#### 3.2 list

list:列表,[a,b,c,d,...]

- lpush userList 1 2 3 4 5：构建一个list，从左边开始存入数据
- rpush userList 1 2 3 4 5：构建一个list，从右边开始存入数据
- lrange list start end：获得数据
- lpop：从左侧开始拿出一个数据
- rpop：从右侧开始拿出一个数据
- llen list：list长度
- lindex list index：获取list下标的值
- lset list index value：把某个下标的值替换
- linsert list before/after value：插入一个新的值
- lrem list num value：删除几个相同数据
- ltrim list start end：截取值，替换原来的list

#### 3.3 hash

hash：类似map，存储结构化数据结构，比如存储一个对象（不能有嵌套对象）

- hset key property value：
  -> hset user name xybh
  -> 创建一个user对象，这个对象中包含name属性，name值为xybh
- hget user name：获得用户对象中name的值
- hmset：设置对象中的多个键值对
  -> hset user age 18 phone 139123123
- hmsetnx：设置对象中的多个键值对，存在则不添加
  -> hset user age 18 phone 139123123
- hmget：获得对象中的多个属性
  -> hmget user age phone
- hgetall user：获得整个对象的内容
- hincrby user age 2：累加属性
- hincrbyfloat user age 2.2：累加属性
- hlen user：有多少个属性
- hexists user age：判断属性是否存在
- hkeys user：获得所有属性
- hvals user：获得所有值
- hdel user：删除对象

#### 3.4 set

set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。

- sadd: 增加新元素
- sismember: 判断元素是否在set中
- sinter: 查看多个set的交集
- spop: 随机删除一个或多个元素并返回
- srandmember:随机获取一个元素
- scard: 获取set的成员数
- key: 返回集合中的所有成员

#### 3.5 zset

sorted set：排序的set，可以去重可以排序，比如可以根据用户积分做排名，积分作为set的一个数值，根据数值可以做排序。


- zadd zset 10 value1 20 value2 30 value3：设置member和对应的分数
- zrange zset 0 -1：查看所有zset中的内容
- zrange zset 0 -1 withscores：带有分数
- zrank zset value：获得对应的下标
- zscore zset value：获得对应的分数
- zcard zset：统计个数
- zcount zset 分数1 分数2：统计个数
- zrangebyscore zset 分数1 分数2：查询分数之间的member(包含分数1 分数2)
- zrangebyscore zset (分数1 (分数2：查询分数之间的member（不包含分数1 和 分数2）
- zrangebyscore zset 分数1 分数2 limit start end：查询分数之间的member(包含分数1 分数2)，获得的结果集再次根据下标区间做查询
- zrem zset value：删除member

> redis sorted sets里面当items内容大于64的时候同时使用了hash和skiplist两种设计实现。这也会为了排序和查找性能做的优化。所以如上可知： 
>
> 添加和删除都需要修改skiplist，所以复杂度为O(log(n))。 
>
> 但是如果仅仅是查找元素的话可以直接使用hash，其复杂度为O(1) 
>
> 其他的range操作复杂度一般为O(log(n))
>
> 当然如果是小于64的时候，因为是采用了ziplist的设计，其时间复杂度为O(n)

### 4、Redis线程模型

<img src="http://image.xybh.online/image-20210201154110193.png" alt="Redis的线程模型" style="zoom: 33%;" />

请求过程:

> Redis-cli发送`Readable/Writable`事件,使用Socket与Redis-server通信,Redis-server使用多路复用器(同步非阻塞)将事件发送到文件事件分配器，文件事件分配器根据请求类型转发至`连接应答处理器/命令请求处理器/命令应答处理器`。

<img src="http://image.xybh.online/Redis%E4%BA%8B%E4%BB%B6%E5%A4%84%E7%90%86%E6%B5%81%E7%A8%8B" alt="img" style="zoom: 33%;" />

### 5、发布与订阅

<img src="http://image.xybh.online/Redis%E5%8F%91%E5%B8%83%E4%B8%8E%E8%AE%A2%E9%98%85.png" alt="image-20210203124116805" style="zoom: 50%;" />

#### 5.1 发布

publish channel messgae: 将消息发送到指定的频道。

#### 5.2 订阅

psubscribe pattern 订阅一个获多个符合给定模式的频道

pubsub subcommand  查看订阅和发布系统状态

subscribe channel  订阅给定的一个或多个频道的信息

unsubscibe channel 退订给定的频道

### 6、Redis读写分离(主从架构)

#### 6.1 主从架构

> Redis主从架构: master节点做到一个分发命令的功能，主节点将数据复制给从库节点。（水平扩展，通过增加服务器来提高性能）

<img src="http://image.xybh.online/Redis%E4%B8%BB%E4%BB%8E%E6%9E%B6%E6%9E%84.png" alt="image-20210203184604139" style="zoom: 50%;" />

#### 6.2 主从原理

>1. 从Redis第一次连接主Redis,使用全量复制
>
>   - 从服务器连接主服务器，发送SYNC命令； 
>   - 主服务器接收到SYNC命名后，开始执行BGSAVE命令生成RDB文件并使用缓冲区记录此后执行的所有写命令； 
>   - 服务器BGSAVE执行完后，向所有从服务器发送快照文件，并在发送期间继续记录被执行的写命令；
>   - 从服务器收到快照文件后丢弃所有旧数据，载入收到的快照； 
>   - 主服务器快照发送完毕后开始向从服务器发送缓冲区中的写命令； 
>   - 从服务器完成对快照的载入，开始接收命令请求，并执行来自主服务器缓冲区的写命令；
>
>2. 增量同步
>   Redis增量复制是指Slave初始化后开始正常工作时主服务器发生的写操作同步到从服务器的过程。 
>   增量复制的过程主要是主服务器每执行一个写命令就会向从服务器发送相同的写命令，从服务器接收并执行收到的写命令。
>
>   
>
>   **Redis主从同步策略**
>   主从刚刚连接的时候，进行全量同步；全同步结束后，进行增量同步。当然，如果有需要，slave 在任何时候都可以发起全量同步。redis的策略是，无论如何，首先会尝试进行增量同步，如不成功，要求从机进行全量同步。(Master必须要开启持久化)

<img src="http://image.xybh.online/Redis%E4%B8%BB%E4%BB%8E%E5%8E%9F%E7%90%86.png" style="zoom: 50%;" />

#### 6.3 主从模式

> 一般采用一主二从，一主多从比较少,因为主从同步需要占用带宽，较多从节点可能会占用较多带宽。

<img src="http://image.xybh.online/Redis%E4%B8%BB%E4%BB%8E%E6%A8%A1%E5%BC%8F.png" alt="image-20210203191422796" style="zoom: 50%;" />

### 7、Redis缓存过期机制

1. (主动)定期删除
   - 定时随机的检查过期的key,如果过期则清理删除。（每秒检查次数在redis.conf中的hz配置)
2. (被动)惰性删除
   - 当客户端请求一个已经过期的key的时候,那么redis会检查这个可以是否过期,如果过期了,则删除,然后返回一个nil。

**如果内存被Redis缓存占用满了怎么办？**

> ​	当内存占用满了后,redis提供了一套缓存淘汰机制:MEMORY MANAGEMENT
>
> - noevivcation:旧缓存永不过期，新缓存设置不了，直接返回错误
> - allkeys-lru:清除所有键中最少使用的旧缓存，然后保存新缓存（推荐使用）
> - allkeys-random:在所有缓存中随机删除（不推荐）
> - volatile-lru:在设置了过期时间的缓存中,清除最少用的旧缓存，然后保存新的缓存
> - volatile-random:在设置了过期时间的缓存中,随机删除缓存
> - volatile-ttl:在设置了过期时间的缓存中,删除即将过期的缓存



### 8、Redis哨兵模式

<img src="http://image.xybh.online/Redis%E5%93%A8%E5%85%B5%E6%A8%A1%E5%BC%8F.png" alt="image-20210204140051369" style="zoom: 50%;" />

1. 配置sentinel.conf(默认端口 26379)
2. sentinel monitor master_name ip_address port quorum(quorum 哨兵检测数量)
3. sentinel auth-pass master_name password

### 9、Redis异常

#### 9.1 缓存穿透

缓存穿透是指缓存和数据库中都没有的数据，而用户不断发起请求，如发起为id为“-1”的数据或id为特别大不存在的数据。这时的用户很可能是攻击者，攻击会导致数据库压力过大。

解决方案：

1. 接口层增加校验,异常请求直接拦截
2. 在缓存和数据库都取不到数据的情况下，可以增加key-null的短期缓存(如30秒,5分钟)
3. 设置布隆过滤器(存在误判率,返回无数据必为无数据,有数据不一定有数据)

#### 9.2 缓存雪崩

 缓存雪崩是指缓存中数据大批量到过期时间，而查询数据量巨大，引起数据库压力过大甚至宕机。和缓存击穿不同的是，缓存击穿指并发查同一条数据，缓存雪崩是不同数据都过期了，很多数据都查不到从而查数据库。

解决方案:

1. 设置不同的过期时间
2. 设置热点信息永不过期
3. 多缓存结合
4. 使用分布式Redis,热点信息存储在不同的Redis中

#### 9.3 缓存击穿

缓存击穿是指缓存中没有但数据库中有的数据（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存没读到数据，又同时去数据库去取数据，引起数据库压力瞬间增大，造成过大压力

解决方案:

1. 设置热点信息永不过期
2. 加互斥锁



## 七、单点登录SSO

单点登录又称为Single Sign On,简称SSO,单点登录可以通过基于用户会话的共享，分为两种：

#### 1、Cookie+Redis实现SSO:

相同顶级域名的单点登录：

- 原理：主要依靠cookie和网站的依赖关系，顶级域名`www.xybh.online`和`*.xybh.online`的cookie值是可以共享的,可以被携带到后档的,比如设置为`.xybh.online`,`.z.xybh.online`.
- 二级域名自己的独立cookie不能被共享,不能被其他二级域名获取

#### 2、CAS

```mermaid
sequenceDiagram
title:SSO系统验证时序图
	客户端 ->> MTV系统:1.初次访问
	MTV系统 ->> MTV系统: 2.验证是否登录
	MTV系统 ->> CAS系统: 3.携带returnUrl跳转至CAS
	CAS系统 ->> CAS系统: 4.验证未登录
	CAS系统 -->> 客户端: 5.显示CAS登录页面
	客户端 ->> CAS系统: 6.用户密码登录
	CAS系统 ->> CAS系统: 7.登录成功
	CAS系统 ->> CAS系统: 8.创建用户会话
	CAS系统 ->> CAS系统: 9.创建用户全局门票
	CAS系统 ->> CAS系统: 10.创建临时票据
	CAS系统 -->> MTV系统: 11.回跳并携带临时票据
	MTV系统 ->> CAS系统: 12.校验临时票据
	CAS系统 ->> CAS系统: 13.校验并成功
	CAS系统 -->> MTV系统: 14.用户会话回传
	MTV系统 ->> MTV系统: 15.保存用户会话
	MTV系统 -->> 客户端: 16.显示登陆成功
	
	
	
```





## 八、工具类

### RedisOperator.java

```java
public class RedisOperator {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 键 操作

    /**
     * 实现 TTL key, 以秒为单位,返回过期时间
     *
     * @param key 键
     * @return 过期时间
     */
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 设置过期时间,单位: 秒
     *
     * @param key     键
     * @param timeout 过期时间
     */
    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key      键
     * @param timeout  过期时间
     * @param timeUnit 时间单元
     */
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 增加key1次
     *
     * @param key 键
     */
    public void incr(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    /**
     * 增加<ref>num</ref>次
     *
     * @param key 键
     * @param num 值
     */
    public void incyByNum(String key, long num) {
        redisTemplate.opsForValue().increment(key, num);
    }

    /**
     * 减少key1次
     *
     * @param key 键
     */
    public void decr(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    public void decrByNum(String key, long num) {
        redisTemplate.opsForValue().decrement(key, num);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    // string 操作

    /**
     * 设置值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置值和过期时间
     *
     * @param key
     * @param value
     * @param timeout
     */
    public void setAndExpire(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取一段范围内的字符
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String getRange(String key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 设置不存在的键
     *
     * @param key
     * @param value
     */
    public void setn(String key, String value) {
        redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 批量查询, 对应mget
     *
     * @param keys
     * @return
     */
    public List<String> mget(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }


    // hash 操作

    public void hset(String key, String filed, String value) {
        redisTemplate.opsForHash().put(key, filed, value);
    }

    public String hget(String key, String filed) {
        return (String) redisTemplate.opsForHash().get(key, filed);
    }

    public void hdel(String key, Object... filed) {
        redisTemplate.opsForHash().delete(key, filed);
    }

    public Map<Object, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    // list 操作

    public void lpush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public void lpush(String key, String... value) {
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void rpush(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void rpush(String key, String... value) {
        redisTemplate.opsForList().rightPushAll(key, value);
    }

    public String lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public String rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }
}
```

## 九、Elasticsearch(ES)

### 1.ES核心术语

- 索引 index				表
- 类型 type                表逻辑类型
- 文档 document            行
- 字段 fields              列