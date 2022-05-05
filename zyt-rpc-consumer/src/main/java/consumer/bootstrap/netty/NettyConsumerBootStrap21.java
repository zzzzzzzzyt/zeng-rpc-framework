package consumer.bootstrap.netty;


import consumer.proxy.RpcNettyClientProxy;
import method.Customer;

/*
    以netty为网络编程框架的消费者端启动类
 */
//进行启动 提供类的方式即可
public class NettyConsumerBootStrap21 {
    public static Customer main(String[] args) throws InterruptedException {
        return (Customer) RpcNettyClientProxy.getBean(Customer.class);
    }
}
