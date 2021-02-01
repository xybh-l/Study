[TOC]

# foodie集群学习

## 一、什么是Nginx

- **Nginx(engine x)**是一个高性能的HTTP和反向代理web服务器，同时也提供IMAP/POP3/SMTP服务。
- 主要功能：反向代理
- 通过配置文件可以实现集群和负载均衡
- 静态资源虚拟化

## 二、常见的服务器

- MS IIS							asp.net
- Weblogic、Jboss 		传统行业 ERP/物流/电信/金融
- Tomcat、Jetty			  J2EE
- Apache、Nginx			静态服务，反向代理
- Netty							  高性能服务器编程			



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

#### main

- user : 工作进程所属用户
- worker_processes:工作进程数量，通常为n-1
- error_log: 保存日志 命令格式 error_log 日志文件名 (debug info notice warn error crit)
- pid: Nginx 启动进程号

#### events

- use epoll; 默认使用epoll, worker进程工作模式
- worker_connections 10240; 每个worker允许连接的客户端最大连接数

#### http

- include				 	导入外部文件
- default_type 	   	文档默认类型
- log_format 		  	日志格式
- access_log				日志文件保存位置
- sendfile 					发送文件
- keeplive_timeout	保持连接
- gzip							gzip压缩

#### server

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

#### 1. Nginx 日志切割(手动)

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



#### 2. Nginx 日志切割(自动)

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

#### 1、轮询

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

#### 2、weight

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

#### 3、ip_hash

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







​	

​	 	











