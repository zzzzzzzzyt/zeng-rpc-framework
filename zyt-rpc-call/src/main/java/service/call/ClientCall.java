package service.call;


import exception.RpcException;
import method.Customer;
import service.bootstrap.ClientBootStrap;

import java.io.IOException;

//通用启动类 将启动的逻辑藏在ClientBootStrap中
public class ClientCall {
    public static void main(String[] args) throws IOException, RpcException {
        Customer customer = ClientBootStrap.start();
        //实现调用
        System.out.println(customer.Hello("success"));
        System.out.println(customer.Bye("fail"));
        System.out.println(customer.Hello("fail"));
    }
}
