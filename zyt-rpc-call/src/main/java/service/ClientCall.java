package service;

import annotation.RpcClientBootStrap;
import annotation.RpcToolsSelector;
import exception.RpcException;
import method.Customer;
import service.call.ChosenClientCall;

import java.io.IOException;

//总客户端启动类  用户调用 什么版本的  和用什么工具 使用什么注册中心  序列化的选择 都可以用这个来玩
//注册中心不能给过去 这样就是重复依赖了
@RpcClientBootStrap(version = "2.1")
@RpcToolsSelector(rpcTool = "Netty")
public class ClientCall {
    public static void main(String[] args) throws RpcException, IOException, InterruptedException {
        //实现调用
        Customer customer = ChosenClientCall.start();

        System.out.println(customer.Hello("success"));
        System.out.println(customer.Bye("fail"));
        System.out.println(customer.Hello("fail"));
    }
}
