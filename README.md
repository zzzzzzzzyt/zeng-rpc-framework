# zeng-rpc-framework
手写自己的RPC框架/Handwritten RPC framework😀

## 技术选型 💻
新技术可持续添加
````
网络传输 : Bio Nio Netty
序列化 : JDK自带序列化 JSON(Jackson Fastjson Gson) Kryo Protobuf Protostuff Hessian FST Avro Thrift 
代理 : 静态代理 动态代理JDK 动态代理CGLIB
注册中心 : Zookeeper Curator SpringCloud Alibaba Nacos
传输协议 : 自己构造
负载均衡 : 随机均衡策略 获取次数均衡策略 一致性哈希均衡策略
压缩机制 : BZip Deflater GZip Lz4 Zip 霍夫曼编码实现编解码器
操作系统 : Linux Windows
容器化 : Docker 
监控中心 : 已经简单的实现在我的用户中心项目 访问地址 http://user.onlyicanstopmyself.top/

其他机制等等 💪
````

作者依托自己所学，慢慢摸索得出，绝无拷贝，大伙儿可以一起来享受搭积木的过程🏫

如果你有什么建议还有什么新的技术可以实现，欢迎提出❗

现已更新至v2.11版本 同时推出了springboot非注解版🎇

关于项目的所有使用信息编写信息更新可以查看手写RPC文档说得都非常详细🔫

md格式的文档 可以私聊我取 qq 836585692 请注明来意🕶

下载上传至其他地方，需注明源地址，否则将会依法溯源👮‍

`尚未完成`

- [ ] **霍夫曼实现自定义编解码器**
- [ ] **客户端与服务端通信协议（数据包结构）重新设计**
- [ ] **另开一个模块解决粘包拆包问题**

**实现思路**

![](https://github.com/Snailclimb/guide-rpc-framework/blob/master/images/rpc-architure-detail.png)




![Alt](https://repobeats.axiom.co/api/embed/b0617848a5c59a1495eb8a88550e9214cdc650b1.svg "Repobeats analytics image")
