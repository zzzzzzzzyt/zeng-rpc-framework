# 手写RPC框架

![img](https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201604%2F05%2F20160405105346_hZKdw.png&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1653394602&t=1243d76abe126c9f3e6417bbfaf3bee2)



==**罗曼-罗兰说过的，这个世上只有一种真正的英雄主义，那就是认清生活的真相，并且仍然热爱它。**==



## RPC架构



![1650802303310](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\1650802303310.png)



## 技术选型



### **网络传输：**

**（为了调用远程方法，就是需要发送网络请求来传递目标类和方法信息以及方法的参数到服务提供方）**

- bio

- nio

- netty

### **序列化：**

**（编写网络应用程序的时候，因为数据在网络中传输的都是二进制字节码数据，在发送数据时需要编码，在接收数据时需要解码）**

- java自带的序列化（之前使用过 这个的话，不支持跨语言平台 同时性能比较差  序列化后的体积比较大）
- kyro
- protobuf（尝试使用这个进行）

### 代理：

**（一开始我也不知道代理有什么用，直接把业务逻辑代码写在那边不就行了吗，但是后面听了韩顺平老师的课后发现，动态代理的目的就是让对象能像调用自己方法一样，调用远端的方法，相当于对于服务调用者是一个黑盒的状态）**

- 静态代理
- 动态代理JDK
- 动态代理Cglib

### **注册中心：**

**（服务端启动的时候将服务名+服务地址+服务端口注册 然后客户端进行调用的时候 就通过查到相应服务的地址进行调用--相当于目录）**

- zookeeper
- nacos

### **传输协议：**

**（传输协议的作用 就是我们发送的信息 要按照我们自己的规定构造 相当于密文传输的感觉 让别人不知道在发送什么 ）**

- 

### **负载均衡：**

**（防止访问量过大，可以将请求分到其他服务提供方上，减少宕机、崩溃的风险）**

- 自己代码实现  

### 其他机制：

- 心跳机制
- 注解开发等等





## 代码实现 : v1.0简易版

`要求简单实现远程调用`

==用最笨的方法实现==

### 技术选型

- 网络传输：nio
- 序列化：java自带序列化

### 客户端

**消费者启动端**

```java
package consumer.bootstrap;

import consumer.nio.NIOClient;

import java.io.IOException;

/*
    以nio为网络编程框架的消费者端启动类
 */
public class NIOConsumerBootStrap {
    public static void main(String[] args) throws IOException {
        NIOClient.start("127.0.0.1",6666);
    }
}

```

**消费者实际业务端**

```java
package consumer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class NIOClient {
    public static void start(String HostName, int PORT) throws IOException{
        start0(HostName,PORT);
    }

    //真正启动在这
    private static void start0(String hostName, int port) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        System.out.println("-----------服务消费方启动-------------");
        socketChannel.configureBlocking(false);
        //建立链接  非阻塞连接  但我们是要等他连接上
        if (!socketChannel.connect(new InetSocketAddress(hostName,port))) {
            while (!socketChannel.finishConnect());
        }
        //创建选择器 进行监听读事件
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        //创建匿名线程进行监听读事件
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    //捕获异常  监听读事件
                    try {
                        if (selector.select(1000)==0)
                        {
                            continue;
                        }
                        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                        while (keyIterator.hasNext())
                        {
                            SelectionKey key = keyIterator.next();
                            ByteBuffer buffer = (ByteBuffer)key.attachment();
                            SocketChannel channel = (SocketChannel)key.channel();
                            int read = 1;
                            //用这个的原因是怕 多线程出现影响
                            StringBuffer stringBuffer = new StringBuffer();
                            while (read!=0)
                            {
                                buffer.clear();
                                read = channel.read(buffer);
                                stringBuffer.append(new String(buffer.array(),0,read));
                            }
                            System.out.println("收到服务端回信"+stringBuffer.toString());
                            keyIterator.remove();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //真正的业务逻辑  等待键盘上的输入 进行发送信息
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            int methodNum = scanner.nextInt();
            String message = scanner.next();
            String msg = new String(methodNum+"#"+message);
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            System.out.println("消息发送");
        }
    }
}
```





### 服务端

**服务提供者启动端**

```java
package provider.bootstrap;

import provider.nio.NIOServer;

import java.io.IOException;

/*
    以nio为网络编程框架的服务提供端启动类
 */
public class NIOProviderBootStrap {
    public static void main(String[] args) throws IOException {
        NIOServer.start(6666);
    }
}

```

**服务提供者实际业务端**

```java
package provider.nio;

import api.ByeService;
import api.HelloService;
import provider.api.ByeServiceImpl;
import provider.api.HelloServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    //启动
    public static void start(int PORT) throws IOException {
        start0(PORT);
    }

    //TODO 当服务消费方下机时  保持开启状态

    /*
        真正启动的业务逻辑在这
        因为这是简易版 那么先把异常丢出去
     */
    private static void start0(int port) throws IOException {
        //创建对应的服务器端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println("-----------服务提供方启动-------------");
        //开启一个选择器 将自己要
        Selector selector = Selector.open();

        //绑定端口开启
        serverSocketChannel.bind(new InetSocketAddress(port));

        //这里注意 要设置非阻塞   阻塞的话  他会一直等待事件或者是异常抛出的时候才会继续 会浪费cpu
        serverSocketChannel.configureBlocking(false);

        //要先设置非阻塞 再注册  如果时先注册再设置非阻塞会报错
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //真正的业务逻辑 就是下面
        //循环等待客户端的连接和检查事件的发生
        while (true)
        {
            //1秒钟无事发生的话  就继续
            if (selector.select(1000)==0)
            {
                continue;
            }

            //获取所有的对象
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext())
            {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable())
                {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("连接到消费端"+socketChannel.socket().getRemoteSocketAddress());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable())
                {
                    //反向获取管道
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    //反向获取Buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    //进行调用方法并返回
                    //获得信息
                    StringBuffer stringBuffer = new StringBuffer();
                    int read = 1;
                    while (read!=0)
                    {
                        //先清空 防止残留
                        buffer.clear();
                        read = socketChannel.read(buffer);
                        //添加的时候  根据读入的数据进行
                        stringBuffer.append(new String(buffer.array(),0,read));
                    }
                    //方法号和信息中间有个#进行分割
                    String msg = stringBuffer.toString();
                    String[] strings = msg.split("#");
                    String response;
                    if (strings.length<2)
                    {
                        //当出现传入错误的时候 报异常
                        System.out.println("传入错误");
                        throw new RuntimeException();
                    }
                    if (strings[0].equals("1"))
                    {
                        HelloService helloService = new HelloServiceImpl();
                        response = helloService.sayHello(strings[1]);
                    }
                    else if (strings[0].equals("2"))
                    {
                        ByeService byeService = new ByeServiceImpl();
                        response = byeService.sayBye(strings[1]);
                    }
                    else
                    {
                        //当出现传入错误的时候 报异常
                        System.out.println("传入错误");
                        throw new RuntimeException();
                    }
                    String responseMsg = "收到信息" + strings[1] + "来自" + socketChannel.socket().getRemoteSocketAddress();
                    System.out.println(responseMsg);
                    //将调用方法后获得的信息回显
                    ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
                    //写回信息
                    socketChannel.write(responseBuffer);
                }
                keyIterator.remove();
            }
        }
    }
}

```



### 序列化

JDK自带序列化



### 依赖引入

`最外层pom引入依赖`

```xml

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>coder.zyt</groupId>
    <artifactId>zeng-rpc-framework</artifactId>
    <packaging>pom</packaging>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>zyt-rpc-consumer</module>
        <module>zyt-rpc-provider</module>
        <module>zyt-rpc-api</module>
        <module>zyt-rpc-common</module>
    </modules>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>
    <dependencies>
        <!--方便构建类-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.22</version>
        </dependency>
        <!--打印日志信息-->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.25</version>
            <scope>test</scope>
        </dependency>
        <!--测试-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>2.5.1</version>
        </dependency>

    </dependencies>
</project>
```



### 遇到的问题

- @Data注解在类上无法使用   `解决`我把我一些没导版本号的依赖加上版本后恢复
- 额外开启一个线程进行监听读事件  第一次可以监听的到 后面就监听不到了     `解决`迭代器iterator一定要记得remove否则就出错了



### 总结

跳过了BIO的方式直接进行了NIO的简易RPC的设计 比较简单



