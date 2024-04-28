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
上面只能为单个路由添加上过滤器，若需要为所有路由添加
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

