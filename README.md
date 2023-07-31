# SUMI BI

### 项目介绍
基于SpringBoot + RabbitMQ + AIGC +（React）的智能数据分析平台。区别于传统BI，用户只需要导入原始数据集，并输入分析诉求，就能够自动生成可视化图表及分析结论，实现降低数据分析的人工成本，提高数据分析效率。

访问地址：http://43.136.59.100 测试账号：kasumi 密码：12345678

### 软件流程
![输入图片说明](src/main/resources/%E6%9C%AA%E5%91%BD%E5%90%8D%E6%96%87%E4%BB%B6.jpg)
### 项目技术栈和特点
- 1. Spring Boot 2.7.13
- 1. Spring MVC
- 1. Spring Boot 调试工具和项目处理器
- 1. Spring AOP 切面编程
- 1. Redis：Redisson限流控制
- 1. MyBatis-Plus 数据库访问结构
- 1. IDEA插件 MyBatisX ： 根据数据库表自动生成
- 1. RabbitMQ：消息队列
- 1. OpenAi API
- 1. JDK 线程池及异步化
- 1. Swagger 项目文档
- 1. Easy Excel：表格数据处理、Hutool工具库 、Apache Common Utils、Gson 解析库、Lombok 注解

### 项目展示
![输入图片说明](src/main/resources/%E6%8D%95%E8%8E%B7.PNG)

### 项目BUG

- AIGC具有一定的随机性，生成的结果不一定是JSON数据格式，导致前端JSON格式数据解析失败

### 项目后续优化改造

- 增加用户中心功能
- 增加收费功能
