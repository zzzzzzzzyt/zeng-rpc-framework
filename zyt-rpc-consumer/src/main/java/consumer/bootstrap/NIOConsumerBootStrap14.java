package consumer.bootstrap;


import consumer.proxy.RpcClientProxy;
import method.Customer;

import java.io.IOException;

/*
    以nio为网络编程框架的消费者端启动类   配合14的集体启动类
 */
public class NIOConsumerBootStrap14 {
    public static void main(String[] args) throws IOException {

        RpcClientProxy clientProxy = new RpcClientProxy();
        Customer customer = (Customer) clientProxy.getBean(Customer.class);
        String response = customer.Hello("success");
        System.out.println(response);
        System.out.println(customer.Bye("fail"));
        System.out.println(customer.Hello("fail"));
    }
}
