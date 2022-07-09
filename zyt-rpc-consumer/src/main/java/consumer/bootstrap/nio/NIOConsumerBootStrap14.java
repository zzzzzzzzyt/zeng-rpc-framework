package consumer.bootstrap.nio;


import consumer.proxy.ClientProxyTool;
import method.Customer;

/*
    以nio为网络编程框架的消费者端启动类   配合14的集体启动类
 */

/**
 * @author 祝英台炸油条
 */
public class NIOConsumerBootStrap14 {
    public static Customer main(String[] args) {
        ClientProxyTool proxy = new ClientProxyTool();
        return (Customer) proxy.getBean(Customer.class);
    }
}
