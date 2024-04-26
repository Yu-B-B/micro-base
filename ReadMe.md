# 远程服务调用

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

### 配置更新


























