package consumer.bootstrap.nio;


import consumer.proxy.ClientProxyTool;
import method.Customer;

import java.io.IOException;

/*
    以nio为网络编程框架的消费者端启动类
 */
public class NIOConsumerBootStrap12 {
    public static Customer main(String[] args) throws IOException {

        ClientProxyTool proxy = new ClientProxyTool();
        return (Customer)proxy.getBean(Customer.class);
    }
}
