# DDD领域驱动脚手架

## DDD分层架构及三层架构对应关系

![三层架构关系](/.image/三层架构对应关系.png "三层架构关系")

## 分层服务调用关系


![分层服务调用关系](/.image/分层服务调用关系.png "分层服务调用关系")

![分层架构调用关系](/.image/分层架构调用关系.png "分层架构调用关系")

## 各个层次功能介绍

*   公用组件

    *   包含全局的工具类、配置、rpc客户端、常量、异常定义及处理、过滤器、拦截器等
*   用户接口层

    *   负责向用户显示信息和接受用户的指令，包括:用户、程序自动化测试、批处理等。等同三层架构的controller
    *   面向用户访间的数据入向接口，可按照不同场景提供不一样的用户接口实现。面向Web的可使用htp restful的方式提供服务，可增加安全认证、权限校验，日志记录等功能，面向微服务的可使用RPC方式提供服务，可增加限流、熔断等功能。
*   应用层

    *   应用层是很薄的一层，理论上不应该有业务规则或逻辑，主要面向用例和流程相关的操作
    *   但应用层又位于领域层之上，它可以协调多个聚合的服务和领域对象完成服务编排和组合，协作完成业务操作
    *   应用层也是微服务之间交互的通道（RPC），它可以调用其它微服务的应用服务，完成微服务之间的服务组合和编排
*   领域层

    *   领域层的作用是实现企业核心业务逻辑，并通过各种校验手段保证业务的正确性。领域层主要体现领域模型的业务能力，用来表达业务概念、业务状态和业务规则
    *   领域层包含聚合根、值对象、领域服务等领域模型中的领域对象，每个领域只处理自己领域的事，领域之间不耦合
    *   领域服务可以组合聚合内的多个实体 (或者值对象)，实现复杂的业务逻辑
*   基础设施层

    *   基础层是贯穿所有层的，它的作用就是为其它各层提供通用的技术和基础服务，包括第三方工具、驱动、消息中间件、网关、文件、缓存以及数据库等。比较常见的功能还是提供数据库持久化
    *   基础层包含基础服务，它采用依赖倒置设计，封装基础资源服务，实现应用层、领域层与基础层的解耦，降低外部资源变化对应用的影响
    *   数据的出向接口，封装数据调用的技术细节，可为其它任意层提供服务，但为了解决耦合的问题采用了依赖倒置原则。其它层只依赖基础设施的接口，于具体实现进行分离

## 代码结构

![代码结构](/.image/代码结构.jpg "代码结构")

目录解析

```
├─main
│  ├─java
│  │  └─com
│  │      └─myth
│  │          ├─common    公共模块
│  │          │  ├─api    通用返回结果对象
│  │          │  ├─client    客户端（rpc）
│  │          │  ├─config    配置类
│  │          │  │  └─mq    mq队列配置
│  │          │  ├─constant    常量
│  │          │  ├─exception    异常类
│  │          │  │  └─handler    全局异常处理
│  │          │  ├─filter    过滤器
│  │          │  ├─interceptor    拦截器
│  │          │  └─util    公用工具类
│  │          ├─order    订单业务
│  │          │  ├─application    应用服务层（可在此调用其他领域的业务服务对象，rpc调用在此）
│  │          │  │  ├─event    mq事件
│  │          │  ├─controller    用户接口层
│  │          │  │  ├─convert    对象转换组装处理类
│  │          │  │  ├─param      请求参数封装类
│  │          │  │  ├─dto    数据传输对象

│  │          │  ├─domain        领域
│  │          │  │  ├─model      领域对象（贫血模型，既类中除setter、getter方法还可包含此领域相关的业务逻辑，减少service复杂度）
│  │          │  │  └─service    领域业务服务层（只处理领域内的业务）
│  │          │  │      └─impl    领域业务服务实现
│  │          │  ├─infrastructure    基础设施层
│  │          │  │  ├─entity    实体对象
│  │          │  │  └─mapper    实体mapper
│  │          │  ├─job    定时任务入口
│  │          │  └─listener    mq监听处理
│  │          └─user    用户业务（下同）
│  │              ├─application
│  │              │  └─event
│  │              ├─controller
│  │              │  ├─convert
│  │              │  ├─dto
│  │              │  └─param
│  │              ├─domain
│  │              │  ├─model
│  │              │  └─service
│  │              │      └─impl
│  │              ├─infrastructure
│  │              │  ├─entity
│  │              │  └─mapper
│  │              ├─job
│  │              └─listener
│  └─resources    资源文件
│   └─mapper    mapperXML文件
│     ├─order    订单相关的xml文件
│     └─user    用户业务相关xml文件
└─test

```

## 脚手架定义规范
1.工程统一继承myth-parent，统一各个组件版本
 - spring-boot-starter-parent 2.7.6
 - spring-cloud-alibaba 2021.0.5.0
 - spring-cloud 2021.0.5

```xml
<parent>
    <groupId>com.myth</groupId>
    <artifactId>myth-parent</artifactId>
    <version>1.0</version>
    <relativePath/>
</parent>
```
2.配置文件定义，nacos配置中心抽取出公共组件配置，项目中有需要可通过扩展配置文件形式引入，无需重复配置
 - redisson-config.yaml redisson配置文件
 - rabbitmq-config.yaml rabbitmq配置文件

``` yaml
spring:
  cloud:
    bootstrap:
      enabled: true
    nacos:
      server-addr: 127.0.0.1:17848
      username: nacos
      password: nacos
      discovery:
        namespace: aeee8abe-56af-4697-a047-0a873a2a38a4
        group: ${NACOS_DISCOVERY_GROUP:demo-local}
      config:
        namespace: aeee8abe-56af-4697-a047-0a873a2a38a4
        group: demo
        file-extension: yaml
        extension-configs:
          - dataId: rabbitmq-config.yaml
            group: common
            refresh: true
          - dataId: redisson-config.yaml
            group: common
            refresh: true
```

3.统一响应结果格式
 - 所有接口返回格式使用CommonResult<T>返回，不同异常返回通过code区分异常场景
 - 分页查询结果使用CommonResult<CommonPage<T>> 返回

``` json
# CommonResult
{
    "code": 200,
    "message": "操作成功",
    "data": "实际响应的结果json"

}

# CommonResult<CommonPage>
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "pageNum": 1,
        "pageSize": 10,
        "totalPage": 5,
        "total": "45",
        "list": []
     }
}
```

4.Redis序列化方式使用Jackson2JsonRedisSerializer

5.工程名定义，统一前缀 myth-业务名称