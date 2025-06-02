# 设计模式

## 设计原则

单一职责：一个类/接口设计的内容尽量满足一个业务所需要的

判断原则：
```
1、属性是否只在单个业务中使用
2、设计中所依赖的内容是否都是需要的
3、设计类中包含的私有方法数量
4、设计中的方法不要只对几个属性做操作
```

开闭原则⭐：设计的抽象类或接口只是作为一个模板，继承实现后的操作不能修改其中的内容。

里氏替换原则：多个类继承或实现一个抽象类/接口时，该抽象类/接口能直接接收到这些类

接口隔离原则：定义的方法根据重要性做分离，避免出现实现类但不实现方法。
做到拆分代码粒度，体现层次；避免代码冗余

依赖倒置原则：以抽象类作为中间层，让上层与下层都依赖抽象层，做到热插拔。

迪米特原则：有关系的内容只用尽量少的东西关联。

## 设计模式

设计模式是对设计原则的具体实现。模式分为创建型、结构性、行为型三种。

## 创建型

### 单例模式
保证服务运行期间，只有一个实例对外提供服务。

将实例的创建交给对象内部完成。

**饿汉式**
```java
class HungryClass{
    public HungryClass(){}
    
    private static HungryClass instance = new HungryClass();
    
    public static getInstance(){
        return instance;
    }
    
}
```
类的加载期间完成实例化，保证instance创建安全。

创建过程较慢，但创建后获取很快。

问题：创建后若不使用造成内容浪费

**懒汉式**

需要使用该类的时候再创建

```java
class LazyClass{
    public LazyClass(){}
    
    private static LazyClass instance = null;
    
    public static getInstance(){
        if(instance == null){
            instance = new LazyClass();
        }
        return instance;
    }
}
```
多线程下，出现多次实例化。

**懒汉式-线程安全**

```java
class LazyClass{
    public LazyClass(){}
    
    private static LazyClass instance = null;
    
    public static synchronized getInstance(){
        if(instance == null){
            instance = new LazyClass();
        }
        return instance;
    }
}

```
锁加载类上，调用时并发度为1，串行执行。

**懒汉式-DCL（双检锁）**

```java
class LazyClass{
    public LazyClass(){}
    
    private volatile static LazyClass instance = null;
    
    public static  getInstance(){
        if(instance == null){
            synchronized (LazyClass.class) {
                if(instance == null) {
                    instance = new LazyClass();
                }
            }
        }
        return instance;
    }
}
```
使用volatile为了方式在初始化过程中发生指令重排

**内部类**

采用内部类，由JVM启动一次后创建对象

**反射方式**

使用内部类作为基类，采用反射创建实例。

单例模式本是通过私有方法隐蔽起来使用时才调用。使用反射后，破坏了属性的私有性。创建多个对象

在构造方法中，进行一轮判断，若已经创建过，抛出异常。

**序列化方式**

对类实现Serializable，实现序列化，创建时通过从文件中读取。

最终序列化还是通过反射方式创建的对象，（反射破坏属性私有性）

解决方法：在类中增加
```
private Object readResolve() {
	return singleton;
}
```

**枚举方式**

枚举满足安全，一个对象，线程安全

```java
public enum EnumSingleClass{
    INSTANCE;

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static EnumSingleClass getInstance(){

        return INSTANCE;
    }
}

```

### 简单工厂

更像是一种编程思想，封装创建过程，通过参数由工厂方法决定创建哪一个。将对象的创建和业务逻辑分开

有新业务创建后，不满足开闭原则，会修改参数判断

### 工厂方法

每个产品都有独立的工厂做创建，通过多态形式创建不同产品工厂，再调用方法实现

每增加一个功能，都要新增一个工厂和具体实现，增加代码复杂度

### 抽象工厂

将一类商品与不同厂商分别抽象，使用中的参数为抽象厂商，抽象厂商封装了抽象产品，

### 建造着

方式一：创建对象由多部件组成
定义一套接口规范为创建者，继承创建者规范后为具体建造者，通过具体建造者完成产品创建，当需要某个产品时，通过领导者完成调度创建者完成具体产品生产

        | product |
        |         |   | detailBuilder1 |
client  | builder |-- |                |             
|         |   | detailBuilder2 |
| direct  |

在实际使用中，会发现直接调用builder也能创建需要的具体产品

方式二：
当需要传递的参数过多来创建对象时，如各种中间件使用时需要指定ip、各类参数。

- 通过构造方法传递参数，数据顺序需要保证一致
- 采用getter/setter方法时，各参数的校验需要保证顺序，防止方法校验中发生空指针。使用set方法对外提供了修改功能，破坏了对象不可变的密封性


相较于使用director做顺序构建，方式二的构建方式更加灵活

### 原型模式

具有相同表示，使用时减少创建次数，采用实现cloneAble并重写clone方法完成。深度拷贝基于二进制流的方式完成，减少资源消耗


减少 计算多、RPC接口得到、慢IO中得到的对象的成本问题