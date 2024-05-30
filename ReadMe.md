#  远程服务调用

传统模式中，想要提供远程调用，Spring中提供了RestTemplate, 用来发送http请求

```java

@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

简单的调用方式为：

```java

@Autowired
private RestTemplate restTemplate;

public void test() {
    String url = "";
    User user = restTemplate.getForObject(url, User.class);
}
```

## Eureka

Eureka可作为服务注册中心，将服务注册到Eureka中，其他服务可以通过服务名来调用服务，减少硬编码的情况。

**服务提供者**：被其他服务调用的服务

**服务消费者**：调用其他模块的服务

一个服务可同时作为服务的提供者和消费者，需要判断当前服务相对与其他服务的角色。

### Eureka注册中心

服务调用中存在的问题：

- 消费者如何获取提供者的地址
- 若存在多个服务提供者，该选择哪一个
- 消费者怎么知道服务提供者的状态

在Eureka中，无论是服务的提供者还是消费者，都是客户端。而Eureka自己为注册中心，用来管理服务的注册和发现。

Eureka的架构：

- Eureka Server：注册中心，用来管理服务的注册和发现
- Eureka Client Service：
    - 服务提供者，启动时注册服务信息到Eureka Server
    - Eureka通过心跳机制（30s）来检查服务是否健康
- Eureka Client Consumer：
    - 服务消费者，从Eureka中获取服务提供者信息，调用相关的服务提供者

作用：

- 消费者如何获取服务提供者的具体信息
    - 提供者启动时想eureka注册自己的信息
    - eureka保存信息
    - 消费者通过服务名称在eureka中获取服务信息
- 若存在多个服务提供者，如何选择
    - 消费者利用负载均衡算法，从列表中随机挑选
- 如何感知服务提供者的健康状态
    - eureka通过心跳机制来检查服务是否健康
    - 若服务不健康，eureka会将服务从注册中心中移除
    - 消费者拉取最新列表信息

### Eureka Server

Eureka是一个独立的服务模块，用来管理服务的注册与发现

**依赖**

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**配置**

```yaml
server:
  port: 8761
spring:
  application:
    name: eureka-server
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

    instance:
      hostname: localhost
```

**启动类**

```java

@EnableEurekaServer // 开启自动装配
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

### 服务注册

无论是消费者还是提供者，都需要注册到Eureka中

**依赖**

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**配置**

```yaml
server:
  port: 8081
spring:
  application:
    name: eureka-client
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### 服务发现/调用

在服务消费者中，通过服务名 + 请求路径获取

```java

@Bean
@LoadBalanced // 负载均衡模式
public RestTemplate restTemplate() {
    return new RestTemplate();
}


@Autowired
private RestTemplate restTemplate;

public void test() {
    String url = "http://user-service/user" + id;
    User user = restTemplate.getForObject(url, User.class);
}
```

### 负载均衡原理

```txt
由LoadBalancerInterceptor实现
其中实现了ClientHttpRequestInterceptor接口，用于拦截用户的所有请求

LoadBalancerInterceptor.intercept(){
  return this.loadBalancer.execute(this.loadBalancer.getName(), requestFactory.createRequest(request, body, execution), this.requestTransformer);
}

BlockingLoadBalancerClient.execute(){
  ServiceInstance serviceInstance = choose(serviceId, lbRequest);
}

BlockingLoadBalancerClient.choose(){
  // 获取实例列表
  List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);
  // 指定choose方法，选择一个实例
  Response<ServiceInstance> loadBalancerResponse = Mono.from(loadBalancer.choose(request)).block();
}

RoundRobinLoadBalancer.choose(){
    // 获取可用服务列表
    ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
            .getIfAvailable(NoopServiceInstanceListSupplier::new);
    // .get()获取服务实例列表的响应式对象
    // .next() 根据当前配置的负载均衡策略，选择一个服务实例
    return supplier.get(request).next()
            .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances));
}

```

### 负载均衡策略

Eureka的默认策略为轮询模式，可通过其他方式实现负载均衡

如：

```java

@Bean
public IRule iRule() {
    return new RandomRule(); // 随机模式
}
```

```xml
ribbon:
        NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

### Eureka饥饿加载

消费者在首次调用提供者的服务时，才会创建LoadBalanceClient对象，创建过程中去拉取服务列表，耗时长

解决方案：通过配置文件配置
```yaml
ribbon:
  eager-load:
    enabled: true
    clients: client1  # 指定对那个或哪些服务开启饥饿加载
      # 或者：
      # - client1
      # - client2

```

# Nacos

国产化组件，比Eureka功能更丰富，支持更多的功能

平时使用时，使用单体方式启动`startup.cmd -m standalone`

## 作为注册中心

### 将服务注册到Nacos中

在父工程中引入管理依赖
```xml
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-alibaba-dependencies</artifactId>
  <version>xxxx</version>
  <type>pom</type>
  <scope>import</scope>
</dependency>
```

将服务注册到Nacos中
```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

```yaml
spring:
  cloud:
    nacos:
      server-addr: localhost:port
```

### Nacos服务分级存储模型

一个服务可包含多个实例，若实例之间存在关联，一个实例挂了，会影响所有实例

Nacos提供了服务分级存储模型，将服务分为多个集群，每个集群下有多个实例

集群：
 - 服务调用尽量选择本地集群服务，跨集群延迟较高
 - 本地集群不可用时再远调

```yaml
nacos:
  discovery:
    cluster-name: xxx # 集群名称
```

### NacosRule负载均衡

要想让服务消费者调用同集群的生产者服务，需要修改负载均衡策略
```yaml
生产者名:
  ribbon:
    NFLoadBalancerRuleClassName: com.alibaba.cloud.nacos.ribbon.NacosRule
```

当同一个集群中存在多个实例，若配置了集群负载均衡策略，将使用随机策略调用实例；
如果消费者和生产者所在的集群中，生产者全部宕机，消费者将远程调用其他集群中的实例

出现远程调用时，将在调用日志中体现出来（提示有远程调用实例的情况）


### Nacos集群实例权重

服务器的性能存在差别，想要体现能者多劳，需要对不同服务设置权重，权重越大访问频率越高

在Nacos控制台中，进入服务详情，对实例进行编辑，修改权重系数，若权重系数为0，则不会被调用（可用于无感知更新）

### 环境隔离-nameSpace

Nacos中服务存储和数据存储最外层都是一个namespace，用于外层隔离

在Nacos中，一个namespace对应一个环境，一个环境中可以有多个服务

修改服务所属的namespace
```yaml 
spring:
  cloud:
    nacos:
      discovery:
        namespace: namespaceId
```

不在一个namespace的服务之间不能调用

### Nacos注册中心原理

- 服务提供者启动，将信息注册到Nacos
- 消费者调用，到Nacos中拉取服务信息，拉取的信息将缓存到本例列表中，30s更新一次
- 消费者在列表中选择一个服务实例进行调用

nacos将服务提供者区分为 临时实例 和 持久实例
- 临时实例：采用心跳机制，30s发送一次心跳，若心跳超时，将实例从列表中移除
- 持久实例：nacos主动发起请求，询问是否可用，不可用时标记为不可用，当实例恢复时，将实例标记为可用
```yaml
spring:
  cloud:
    nacos:
      discovery:
        ephemeral: false # 非临时实例
```

当消费者缓存了服务列表后出现生产者不可用，消费者未及时感知，将导致调用失败
- Nacos主动推送变更消息



| Eureka     | Nacos                                                  |
| ---------- | ------------------------------------------------------ |
|            | 服务端可主动检测提供者状态：临时用心跳，持久用主动     |
|            | 临时不健康被剔除，持久不会被剔除                       |
|            | 支持列表主动推送，及时更新                             |
| 采用AP方式 | 集群默认采用AP方式，当集群存在非临时实例时，采用CP模式 |



## 作为配置中心

### 统一配置管理

若一个配置文件关联多个实例，在调整时不方便且都需要重启。操作麻烦

使用统一配置文件管理，可实现配置更改热更新

在nacos管理中心---→配置中心---→新建配置



### 服务配置拉取

项目启动过程：项目启动----读取本地配置文件----创建spring容器----加载bean

使用统一配置后：项目启动----读取nacos配置文件----读取本地配置文件----

若想要读取nacos中文件，需要将统一配置文件的信息写入本地bootstrap.yaml文件中，提高优先级

引入服务的配置管理
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```
bootstrap.yaml
```yaml
spring:
  application:
    name: serverName # 服务名
  profiles:
    active: dev # 开发环境
  cloud:
    nacos:
      server-addr: localhost:port
      config:
        file-extension: yaml # 文件后缀
        group: DEFAULT_GROUP
        namespace: namespaceId
```
根据服务名+开发环境+文件后缀，构成统一配置管理中的serverName-dev.yaml文件

### 配置热更新

项目发布后，修改统一配置文件后，应完成配置的自动更新

**方式一：**

在使用@Value注解的类上添加@RefreshScope，当配置文件发生变化时，自动刷新
```java
@RefreshScope
public class XxxController {
    @Value("${xxx}")
    private String xxx;
}
```
当配置文件发生变化时，系统将感知到变化，更新配置

**方式二：**

使用配置类完成配置文件属性映射

```java
import java.beans.BeanProperty;

@Component
@ConfigurationProperties(prefix = "yyy")
public class PropertiesConfig {
    private String xxx;
}
```

### 配置共享

微服务启动时会从nacos中读取多个配置文件
- [sping.application.name]-[spring.profiles.active].yaml
- [spring.application.name].yaml
其中[spring.application.name].yaml 文件一定会加载，若[spring.profiles.active]不存在时，
spring启动也会加载，所以可将公共配置放入其中

若[spring.application.name]-[spring.profiles.active].yaml、[spring.application.name].yaml、和本地配置文件存在相同配置时，
优先级为：[spring.application.name]-[spring.profiles.active].yaml > [spring.application.name].yaml > 本地配置文件

## nacos集群搭建

为让Nacos在线上时高可用，需要配置集群

```
1. 数据库配置
2. Nacos配置
3. Nginx负载均衡
4. 修改代码配置文件
```

Nacos配置：

- 在conf中，修改配置文件cluster.conf.example，将文件名修改为cluster.conf
- 添加集群中每个节点信息
  - 192.168.1.2 8818
  - 192.168.1.2 8819
  - 192.168.1.2 8817
- 打开数据库配置
  - spring.datasource.platform = mysql
  - 数据库配置信息

Nginx配置
  - 修改conf/nginx.conf
    ```xml
    upstream nacos-cluster {
      server 192.168.1.2:8818
      ...
    }
    server {
      listen 80;
      server_name localhost;
      location /nacos {
      proxy_pass http://nacos-cluster;
    }
    ```

修改代码配置文件
  - 修改nacos配置文件中的server-addr为nginx地址


# Feign

传统http请求，采用硬编码方式

```java
String url = "http://ip+port/api";
User user = restTemplate.getForObject(url,User.class);
```

使用ribbon优化

```java
String url = "http://serverName/api";
User user = restTemplate.getForObject(url,User.class);
```

依然存在的问题

- 可读性差，编程体验不统一
- 若请求参数复杂，难以维护

## 介绍

是一个声明式的http客户端，用于简化http请求

## 使用

**1. 引入依赖**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
**2. 开启注解**
```java
@EnableFeignClients
// ...
public class XxxApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxxApplication.class, args);
    }
}
```
**3. 编写feign客户端**
```java
// 当order调用user服务时，使用feign
@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/user/{id}")
    User getUser(@PathVariable("id") Long id);
}
```

在远程调用时，还做到了负载均衡，feign中已集成了ribbon

## 自定义配置

Feign运行自定义配置

| 类型                | 作用         |                             |
| ------------------- | ------------ |-----------------------------|
| feign.Logger.Level  | 日志级别     | NONE，BASEIC，HEADERS，FULL    |
| feign.codec.Decoder | 响应结果解析 | 对远程调用结果做解析                  |
| feign.codec.Encoder | 请求参数编码 |                             |
| feign.Contract      | 注解格式     |                             |
| feign.Retryer       | 失败重试机制 | 默认没有，但会使用Ribbon的重试机制，避免网络波动 |

**方式一：配置文件**

全局生效
```propertiess
feign.client.config.default.loggerLevel: full
```
局部生效
```properties
feign.client.config.user-service.loggerLevel: full
```

**方式二：配置类**

首先声明一个Bean
```java
public class FeignConfig {
    @Bean
    public Logger.Level level() {
        return Logger.Level.BASIC;
    }
}
```

① 若是全局,放在@EnableFeignClients后
```java
@EnableFeignClients(defaultConfiguration = FeignConfig.class)
```

② 若是局部，放在FeignClient注解中
```java
@FeignClient(name = "user-service", configuration = FeignConfig.class)
```

## 性能优化

feign在发送请求时，底层客户端可通过三种方式发送请求
- URLConnection: 默认方式，不支持连接池（连接池：减少创建连接的三次握手和断开连接的四次挥手）
- Apache HttpClient: 支持连接池
- OkHttp: 支持连接池

优化Feign性能包括：
- 使用连接池代替默认的URLConnection
- 日志级别调整，减少日志输出

### 连接池配置-HttpClient

```xml
<dependency>
  <groupId>io.github.openfeign</groupId>
  <artifactId>feign-httpclient</artifactId>
</dependency>
```
配置连接池
```yaml
feign:
  client:
    config:
      default:
        loggerLevel: BASIC # 日志级别
  httpclient:
    enabled: true # 开启feign对httpclient的支持
    max-connections: 200 # 最大连接数，根据实际压测调整
    max-connections-per-route: 50 # 每个路由最大连接数，根据实际压测调整
```

# GateWay网关

## 介绍，作用

为什么需要：
- 进行身份和权限校验
- 服务路由，负载均衡
- 请求限流

实现方式：（SpringCloud）
- Zuul
- GateWay

Zuul：基于Servlet，使用阻塞IO，性能较差

GateWay：基于Spring5中WebFlux，响应式实现，性能较好

## 搭建

网关自己也是一个服务，需要创建一个独立的模块，也需要注册到nacos中

**1. 引入依赖**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

### 请求路由配置
```properties
server.port=8080
spring.application.name=gateway
spring.cloud.nacos.discovery.server-addr=localhost:8848
spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://user-service
spring.cloud.gateway.routes[0].predicates[0]=Path=/user/**


#spring:
#    cloud:
#        gateway:
#            routes:
#                - id: user-service
#                  uri: lb://user-service # lb:loadbalance
#                  predicates: # 路由断言，判断请求是否满足路由规则条件
#                    - Path=/user/** # 如果请求是以/user开口，则路由到user-service
```

<span style="color:red">使用Gateway后，还需要使用Feign吗？</span>
- 当直接通过URL请求服务时，若Gateway中配置了路由，将直接拿到路由的信息
- 如果是在业务代码中存在远程的调用，还是需要使用Feign

## 断言工厂

网关路由内容包括：
- 路由id：唯一标识
- uri：路由目的地，支持lb和http
- predicates：路由断言，判断请求是否满足路由规则条件
- filters：过滤器，对请求进行处理

### 断言工厂

配置文件中写的断言内容，这些断言字符串被断言工厂读取并处理，转变为路由判断的条件

| 名称 | des           | example                                                                                   |
| --- |---------------|-------------------------------------------------------------------------------------------|
| After | 请求时间在某个时间之后   | After=2020-01-01T00:00:00+08:00[Asia/Shanghai]                                            |
| Before | 请求时间在某个时间之前   | Before=2020-01-01T00:00:00+08:00[Asia/Shanghai]                                           |
| Between | 请求时间在某个时间之间   | Between=2020-01-01T00:00:00+08:00[Asia/Shanghai],2020-01-02T00:00:00+08:00[Asia/Shanghai] |
| Cookie | 请求需携带某个cookie | - Cookie=name                                                                             |
| Header | 请求需包含某些header | - Header=X-Request-Id, \d+                                                                |
| Host | 请求需时访问某个host  | - Host=**.somehost.org                                                                    |
| Method | 请求的方法需为指定方式   | - Method=GET                                                                              |
| Path | 请求的路径需包含指定规则  | - Path=/foo/{segment}, /foo/\d+                                                           |
| Query | 请求的参数需包含指定参数  | - Query=foo, ba.+, ba.*                                                                   |
| RemoteAddr | 请求的ip指定范围     | - RemoteAddr= ip/24                                                                       |               
| Weight | 权重处理          | Weight=group1, 8                                                                          |


## 过滤器GatewayFilter

对进入网关的请求和微服务返回的响应做处理
- 对经过网关的请求添加请求头
- 对返回的响应结果拿出请求头(etc)做判断

使用配置文件添加
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/user/**
          filters:
            - AddRequestHeader=X-Request-Red, blue
```
上面只能为单个路由添加上过滤器，若需要为所有路由添加，使用`默认过滤器`
```yaml
spring:
  cloud:
    gateway:
      routes:
        # ...
      default-filters:
        - AddRequestHeader=X-Request-Red, blue
```

## 全局过滤器

前面使用的过滤器都是局部过滤器，在一些特殊情况下，使用并不方便，需要使用全局过滤器完成自定义逻辑

实现GlobalFilter接口
```java
public interface GlobalFilter {
    // exchange，请求上下文，获取request,response信息
    // chain，过滤器链，用于调用下一个过滤器
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain);
}
```

例:
```java
public class AuthourizeFilter implements GlobalFilter{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求信息
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String,String> params = request.getQueryParams();
        // 获取参数中的authentication
        String auth = params.getFirst("authentication");
        if("admin".equals(auth)) {
            // 放行
            return chain.filter(exchange);
        }
        // 不放行
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
```

## 过滤器执行顺序

请求进入网关会遇到的过滤器包括：当前路由过滤器，DefaultFilter，GlobalFilter

在过程中，会将三个过滤器放在一个集合中，按照顺序依次执行

无论是路由过滤器还是默认过滤器，其本质都是（AddRequestHeaderGatewayFilterFactory），它在读取配置文件后生成GatewayFilter，
而对于全局过滤器，虽然类型和路由过滤器与默认过滤器不一致， 但是在网关中（FilteringWebHandler）中，存在一个过滤器适配器（GatewayFilterAdapter），
其内存接收了全局过滤器（GlobalFilter），可将GlobalFilter适配为GatewayFilter

所以，上述说能将三个过滤器放在一个集合中，集合类型为GatewayFilter

执行顺序:
- 过滤器指定的order值，order越小，优先级越高
- 全局过滤器通过实现Ordered接口/添加@Order注解指定order值
- 路由过滤器和defaultFilter的order有Spring指定，默认按照声明顺序从1递增
- 当过滤器order值一样时，按照那个defaultFilter>路由过滤器>GlobalFilter的顺序执行

## 跨域问题处理

跨域问题包括
- 域名跨域
- 域名相同后端口不同

网关处理跨域问题：
```yaml
spring:
  cloud:
    gateway:
      globalcors:
        add-to-simple-url-handler-mapping: true # 解决options请求被拦截问题
        cors-configurations:
          '[/**]':
            allowedOrigins:  # 允许哪些域名，全部为"*"
              - "http://localhost:7491"
              - "http://localhost:7492"
            allowedMethods: # 允许的跨域ajax请求方式
              - "GET"
              - "POST"
              - "PUT"
              - "DELETE"
            allowedHeaders: "*" # 允许的请求头
            allowCredentials: true # 允许携带cookie
            maxAge: 1800 # 有效时间
```

前端启动服务命令：
live-server --port=7491


# Docker

# MQ

## 同步与异步

同步：实时调用，时效性高，微服务中Feign的调用为同步调用，需要等待接口返回结果
- 耦合度高
- 性能下降
- 资源浪费：调用时需等待相应
- 级联失败：调用链中一个失败整个失败


异步：事件驱动模式，引入Broker（事件代理者），当前置完成需要用到后续服务时，直接通知Broker，Broker再通知后续服务，不在等待后续执行完成
- 服务解耦
- 性能提高，吞吐量提高
- 服务无强依赖
- 流量消峰
- 过于依赖Broker，需要保证Broker的可靠，安全，吞吐能力
- 架构负载，业务中无明显流程，不好追踪


## 消息模型

1. 直接通过队列发送消息
- 基本消息队列（BasicQueue）
- 工作消息队列（WorkQueue）

2. 发布订阅（根据交换机可细分）
- 广播（Fanout）
- 路由（Direct）
- 主题（Topic）

3. 消息队列中角色
- 生产者：将消息发送到队列queue
- 消费者：接收接收并缓存消息
- 订阅队列，处理队列中的消息

### 基本消息队列

生产者步骤：
- 建立连接
- 创建通道
- 声明队列
- 发送消息
- 关闭通道和连接

消费者步骤：
- 建立连接
- 创建通道
- 声明队列
- 接收消息

在消费者接收消息中，提前声明如何处理消息，将行为挂在到队列中，当队列中存在消息，函数执行（回调函数），所以接收消息是异步过程

### SPringAMQP

AMQP：Advanced Message Queuing Protocol，高级消息队列协议，提供消息队列规范

SpringAMQP：Spring对AMQP的封装，提供了一套操作AMQP的模板

**依赖**
```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-amqp</artifactId>
  </dependency>
```

**配置**
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

**生产者**
```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void send() {
    rabbitTemplate.convertAndSend("queue", "hello");
}
```

**消费者**
```java
@RabbitListener(queues = "queue")
public void receive(String message) {
    System.out.println("接收到消息：" + message);
}
```

### 工作队列

一个队列多个消费者，消费者之间存在合作关系，解决当消息数量大约消费者处理能力，导致消息堆积，造成消息丢失

**生产者**
```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void send() {
    for(int i = 0; i < 50; i++){
        rabbitTemplate.convertAndSend("work-queue", "hello");
        Thread.sleep(10);
    }
}
```

**消费者**
```java
@RabbitListener(queues = "work-queue")
public void receive1(String message) {
    System.out.println("消费者1接收到消息：" + message);
}

@RabbitListener(queues = "work-queue")
public void receive2(String message) {
    System.out.println("消费者2接收到消息：" + message);
}
```

此时消费者1和消费者2将平均接收到消息，这是消息预取机制，取消消息预取机制
- listener.simple.prefetch=1

### 发布订阅

与之前的方式相比，发布订阅可将同一条消息发送给多个消费者。实现方式是加入交换机（exchange）

消息发给一个队列还是多个队列，都由交换机决定，常见的订阅模型：Fanout、Direct、Topic

交换机只负责消息的转发，不存储消息，消息存储在队列中，路由失败则消息丢失

#### Fanout

将消息路由到每一个跟其绑定的队列

步骤：
- 在消费者服务中，声明队列和交换机，并进行绑定
- 在消费者服务中，监听队列一和队列二

**消费者**
```java
public class FanoutConsumer{
  @Bean
  public FanoutExchange fanoutExchange() {
      return new FanoutExchange("fanout-exchange");
  }
  
  @Bean
  public Queue queue1() {
      return new Queue("queue1");
  }
    
  @Bean
  public Binding binding1(Queue fanout1,FanoutExchange  change1) {
      return BindingBuilder.bind(fanout1).to(change1);
  }
  // ...queue2,fanout2,binding2
}
```

如何知道消费者真实拿到了数据，通过监听队列

```java
@RabbitListener(queues = "queue1")
public void receive1(String message) {
    System.out.println("消费者1接收到消息：" + message);
}
```

**生产者**
```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void send() {
    // exchange, routingKey, message
    rabbitTemplate.convertAndSend("fanout-exchange", "", "hello");
}
```

#### Direct

将接收到的消息按照规则路由到指定的Queue，可模拟Fanout

步骤：
- 每个queue都与exchange设置一个bindingKey
- 发布者发布消息时，指定消息的RoutingKey
- exchange将消息路由到BingKey与消息RoutingKey一致的队列

=>

- 通过RabbitListener声明Exchange，Queue，RoutingKey
- 在消费者中，监听队列1和2
- 生产者想交换机发送消息

**消费者**
```java
@RabbitListener(bindings= @QueueBinding(
    value = @Queue(name = "direct-queue1"),
    exchange = @Exchange(value = "direct-exchange", type = ExchangeTypes.DIRECT),
    key = "direct1" // 或 key = {"direct1","direct2"}
))
public void receive1(String message) {
    System.out.println("消费者1接收到消息：" + message);
}
```

#### Topic

与Direct类似，但是RoutingKey是以多单词区分，以.分割，*代表一个单词，#代表多个单词，如：user.*、user.#、user.create

**消费者**
```java
@RabbitListener(bindings= @QueueBinding(
    value = @Queue(name = "topic-queue1"),
    exchange = @Exchange(value = "topic-exchange", type = ExchangeTypes.TOPIC),
    key = "user.*"))
public void receive1(String message) {
    System.out.println("消费者1接收到消息：" + message);
}
```

**生产者**
```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void send() {
    rabbitTemplate.convertAndSend("topic-exchange", "user.create", "hello");
}
```

### 消息转换器

在RabbitTemplate发送消息时，消息类型都是Object，所以可以发任意消息给消息队列

但是在消费者接收消息时，需要将消息转换为指定类型，所以需要消息转换器

Spring中通过org.springframework.amqp.support.converter.MessageConverter处理，
消息对垒中默认使用SimpleMessageConverter（基于JDK的ObjectOutputStream）完成序列化

```xml
<dependency>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
  <version>2.9.10</version>
</dependency>
```

在发送者中声明使用MessageConverter
```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void send() {
    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    rabbitTemplate.convertAndSend("queue", new User(1, "张三"));
}
```

全局使用
```java
@Bean
public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
}
```

# ES

负责数据存储，搜索，分析

## ES基本概念

- Document：文档，数据的最小单元，会被序列化为json格式
- Index：索引，相同类型的文档集合
- Mapping：映射，定义文档的字段类型

| MySQL  | Elasticsearch | des                               |
|--------|---------------|-----------------------------------|
| Table  | Index         | 索引，文档集合，类似于数据库的表                  |
| Row    | Docuemtn      | 文档，一条条数据，类似数据库中的行，文档格式都是Json      |
| Colums | Field         | 字段，Json文档中的字段，类似数据库中的列            |
| Schema | mapping       | Mappin是索引文档的索引，如：字段类型约束，类似数据库中的表  |
| Sql    | DEL           | DSL，es提供的JSON风格请求语句,用来操作es,实现CRUD |

MySQL擅长事务类型操作，可确保数据的安全和一致性

ES：擅长海量数据的搜索、分析、计算

## 正向索引与倒排索引

传统数据库（MySql）采用正向索引，若插叙非索引字段的数据，将逐行扫描

倒排索引，创建时形成新的表（词条（term），文档Id），文档：每条数据就是一个文档；词条：按照语义分成词语

![image-20240530154736310](E:\File\JavaProject\XF\cloud-demo\images\image-20240530154736310.png)

当开始查询时，先根据用户输入的句子进行分词，使用词条到倒排索引中查找，得到文档Id

// TODO:

# Sentinel

## 雪崩问题

微服务调用链路中某个服务出现故障，引起整个链路中所有微服务都不可用

处理方式：
- 超时处理：设置超时时间，请求超过时间未响应就返回错误信息
- 舱壁处理：设定每个线程可使用的线程数，避免tomcat资源耗尽--线程隔离
- 熔断降级：由断路器统计业务执行异常比例，如果超出阈值则熔断该业务，拦截访问该业务的所有请求

- 流量控制：限制业务访问的QPS，避免服务因流量突增而故障--预防雪崩


Sentinel是按照服务能承受的频率释放请求

## 限流规则

### 簇点链路

项目内的调用链路，链路中被监控的每个接口就是一个资源，默认情况下sentinel监控SpringMvC中的每一个端点，因此SpringMVC的每一个端点就是调用链路中的一个资源