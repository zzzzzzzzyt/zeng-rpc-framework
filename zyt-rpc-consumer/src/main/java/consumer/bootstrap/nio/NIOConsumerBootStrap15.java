package consumer.bootstrap.nio;


import consumer.proxy.RpcClientProxy;
import method.Customer;

import java.io.IOException;

/*
    以nio为网络编程框架的消费者端启动类   配合15的集体启动类
 */

public class NIOConsumerBootStrap15{
    public static Customer main(String[] args) throws IOException {

        RpcClientProxy clientProxy = new RpcClientProxy();
        return (Customer) clientProxy.getBean(Customer.class);

    }
}