# restboot-demo
##介绍
该系统为的权限管理系统 为个人经验累计的练手项目
11.28 补充了模拟上传第三方图片服务器响应流程
##技术架构：
开源框架：springboot mybatis-plus shiro jwt

文档助手：swagger swagger-bootstrap-ui(美化ui)

代码生成器：lombok velocity-engine modelmapper

数据库：mysql Innodb

日志：log4j

其他技术：redis/ehcache二级缓存(@Cacheable/@CachePut/@CacheEvict) 及dao层namespace缓存

数据处理：jackson fastjson

##package jar
dev 

skip test => clean => package

##run jar
restboot 8888.bat
````
@echo off
title restboot 8888
java -jar -Xms200m -Xmx500m restboot-0.0.1-SNAPSHOT.jar --server.port=8888 --spring.profiles.active=dev
````

##redis 过期key监听需要配置
需要设置redis的配置文件redis.windows-service.conf 设置notify-keyspace-events "Ex"