package provider.bootstrap.netty;


import lombok.extern.slf4j.Slf4j;
import monitor.RpcMonitor;
import monitor.RpcMonitorOperator;
import provider.netty.NettyServer24;

import java.util.concurrent.atomic.AtomicInteger;


/*
    以netty为网络编程框架的服务提供端启动类
 */
//实现方法和之前都是比较一致的 每个对象要开一个线程去执行呢 原因是我们会启动同步等他们关闭 才出来 这样才能关闭对应的管道

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyProviderBootStrap24 {
    static volatile AtomicInteger port = new AtomicInteger(6666); //对应的端口 要传过去 注册到注册中心去


    public static void main(String[] args) {
        //首先对原先数据库中的数据进行清空  因为这是重新启动 服务提供端口 所以需要重新计算
        RpcMonitorOperator rpcMonitorOperator = new RpcMonitorOperator();
        rpcMonitorOperator.deleteAll();

        //直接在这里将对应的方法什么的进行分开 然后传过去
        String methods = args[0];
        String nums = args[1];
        String[] methodArray = methods.split(",");
        String[] methodNumArray = nums.split(",");
        //进行创建  可能会出问题 这边的端口
        for (int i = 0; i < methodArray.length; ++i) {
            String methodName = methodArray[i];
            for (int methodNum = 0; methodNum < Integer.parseInt(methodNumArray[i]); methodNum++) {
                RpcMonitor rpcMonitor = new RpcMonitor();
                //TODO 这里之后还可以继续进行更改 因为这块的话 如果放在服务器上 那么就可以采用服务器相关的设置
                rpcMonitor.setMethodName("127.0.0.1:" + port);
                rpcMonitor.setMethodDescription(methodName);
                rpcMonitorOperator.addServer(rpcMonitor);
                int nowPort = port.get();
                //因为下面这个开启一个线程 会慢一点
                new Thread(() -> NettyServer24.start(methodName, nowPort)).start();
                port.incrementAndGet();
            }
        }
    }
}
