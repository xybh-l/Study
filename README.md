[TOC]

# 自我介绍

面试官您好！我叫蓝旺，目前是一名软件工程专业大三学生。我在大学期间学习了Java及Spring，SpringBoot，SpringMVC，Mybatis等框架，熟悉使用MySQL和Redis进行开发。在校期间曾参与一个企业命题的项目，这个项目主要使用了SpringBoot、SpringMVC、MyBatis和Spring Security框架，采用MySQL作为主要数据库，使用Redis做了一个数据缓存，我在其中主要担任了一个后端负责人的角色，负责协调后端人员的任务分配和前端人员的沟通协调以及重点难点的突破。另外在大学期间也参加了一些学科竞赛，取得了ACM-ICPC江西省省金等奖项，此外我热爱技术，习惯总结，喜欢通过写博客来整理分享所学知识，对代码充满激情，曾为完成项目预计目标，通宵编程。生活中我也是个比较乐观开朗的人，学习累了就会去打打羽毛球，我一直都非常想加入贵公司，因为我觉得贵公司的技术氛围我非常喜欢，期待能与你一起工作。



# 2021.1.13 百度智能交通业务二部 Java开发工程师

2021 1.13 了解基本情况

百度的工作效率是蛮快的，快的我都没做好准备，中午12点多进行的内推，下午3点就有公司的hr来了解一些基本情况了。花了将近30分钟问了一些基本的问题。

1. 自我介绍
2. 对软件工程的理解
3. 软件工程和其他计算机学科的区别和共同点
4. 为什么会选软件工程
5. 对自己项目的一个背景介绍
6. 什么时候可以入职，可以实习多久

这也不能算是一次面试吧，可能就是初步了解一下个人的性格和可以实习的时间，最后简单介绍了他们部门，说后续有通知会联系我。

2021 1.15 一面

1. 自我介绍
2. String、StringBuilder、StringBuffer区别
3. ArrayList、LinkedList、Vector的区别
4. ArrayList和Vector的扩容机制
5. HashMap的底层实现
6. 创建线程池、线程池的运行机制和拒绝策略
7. synchronized的实现和机制，锁升级机制
8. 了解JVM吗
9. 了解计算机网络吗（只问了我一下就没接着往下问了）
10. MySQL索引底层实现和在InnoDB中的实现（只答出了B+ Tree，忘记了Hash实现，后面直接紧张忘记了在InnoDB中是怎么实现的）
11. Redis持久化的方式
12. AOF备份文件过大怎么办
13. 合并两个有序链表

# 2021 1.20 字节跳动电商业务-后端开发实习生

2021 1.20 一面

1. 物业管理系统难点

   1.1 需求分析，数据库架构设计、审批流程

   1.2 前后端交互

2. linux操作系统

   2.1 杀掉一个进程 kill -9 pid

   2.2 怎么实现这个命令，进程在系统中的结构：PCB+栈+数据块

   2.3 进程的交互，linux进程交互的方式

   2.4 32、64位操作系统区别

   2.5 cpu交互的位数

   2.6 内存映射 

3. [www.douyin.com](http://www.douyin.com/) ，浏览器提示找不到IP地址，原因分析

   3.1 DNS解析故障。 

   3.2 DNS详细解析过程

   3.3 DNS传输层协议。UDP协议

   3.4 TCP三次确认了双方的什么能力？

4. mysql

   4.1统计数据表t_time中每个小时记录数

   

   ​    id  p_date

   ​    1  2020/08/29 00:10:10

   ​    2  2020/08/29 01:10:10

   ​    3  2020/08/29 01:10:10

   ​    4  2020/08/29 02:10:10

   ​    5  2020/08/29 01:10:10

   

   4.2 sql语句的执行过程

5. java 安全的停止一个线程

   ```java
   Thread.interrupt(); 	//中断线程
   Thread.isInterrupted();	//判断是否中断
   Thread.interrupted();	//判断是否中断,并且清除当前中断状态
   ```

   

   

   

6. 旋转无环链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数
     in:1->2->3->4->5->NULL, k = 2

     out:4->5->1->2->3->NULL

    public ListNode rotateRight(ListNode head, int k){
     return null;
   }

   

7. 给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）
   使得它们的和等于 n。你需要让组成和的完全平方数的个数最少

   输入: n = 12 输出 3 

# 2021.3.4 支付宝

2021.3.4 一面(1个小时19分钟)

1. 自我介绍
2. 说说你项目中的建的表
   1. 为什么要分多个表,放在一个表里不行吗?
   2. 索引的原理
   3. 如何找出区域里总租金第二的区域
3. MySQL事务的四大属性
4. 事务的隔离等级
5. MYSQL默认的隔离机制
6. 为什么InnoDB的next_key机制为什么能避免幻读
7. 说说什么是幻读,那你能知道next_key的原理了吗?
8. 说说你项目里的难点
9. 为什么要用逻辑外键,而不用数据库的外键
10. 怎么建索引,有什么技巧
11. 说说悲观锁和乐观锁的区别
12. 什么是CAS,为什么其他线程能看见这个共享变量
13. ABA问题怎么解决
14. 线程和进程的区别
15. 进程的通信方式,说说你用过什么方式,为什么用信号量,没有用过socket吗?
16. 怎么创建线程, 继承Thread类和实现Runnable接口的区别
17. 线程的状态你知道吗?Running怎么切换成Ready?
18. Sleep()和Wait()的区别,怎么取消wait()?
19. 用过线程池吗?线程池的几大重要参数知道吗?
20. 线程池的拒绝策略
21. 你就只用过自己定义的线程池吗?JUC下自带的线程池
22. 你用过CacheThreadPool吗?为什么不用?
23. TCP的三次握手和四次挥手
24. 为什么要使用三次握手?
25. TCP首部的组成
26. 类的加载机制,双亲委派模型
27. 双亲委派模型是为了解决什么问题?怎么破坏双亲委派模型?你知道获取父类加载器的函数吗?
28. JVM的内存模型
29. 你说说什么是堆吧,分代模型知道吗?
30. 为什么要用两个Survivor区,一个不行吗?我只回答了它使用的是复制算法.
31. 为什么用复制算法就要两个Survivor区,老年代为什么不用这种算法,用标记整理算法不行吗?
32. 你用过Spring,那你能说说AOP吗?AOP是怎么实现的,代理模式的三种实现方法你知道吗?
33. Bean的加载机制知道吗?
34. 我看你打过算法比赛，你能说说你知道的算法吗？DFS,BFS,DP,博弈论,数论
35. 你在队里是负责什么算法的?
36. 你能跟我讲讲什么是Dijkstra算法吗?
37. 最后一题算法题,给定一个字符串,求包含26个字母的最短子串(双指针+哈希)
38. 你说说你怎么和团队里的人沟通协调的,怎么分配任务?
39. 你还有什么问我的吗?