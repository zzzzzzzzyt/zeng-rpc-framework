package service.nio_call;


import exception.RpcException;
import method.Customer;
import service.nio_bootstrap.NIOClientBootStrap;

import java.io.IOException;

//通用启动类 将启动的逻辑藏在ClientBootStrap中
public class NIOClientCall {
    public static void main(String[] args) throws IOException, RpcException {
        Customer customer = NIOClientBootStrap.start();
        //实现调用
        System.out.println(customer.Hello("success"));
        System.out.println(customer.Bye("fail"));
        System.out.println(customer.Hello("fail"));
    }
}
