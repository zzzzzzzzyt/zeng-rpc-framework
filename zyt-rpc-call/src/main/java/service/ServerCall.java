package service;

import annotation.RpcMethodCluster;
import annotation.RpcServerBootStrap;
import annotation.RpcToolsSelector;
import org.apache.zookeeper.KeeperException;
import service.call.ChosenServerCall;

import java.io.IOException;

//总服务端启动类 用户调用  注解是 注册什么方法进去
//调用的是什么版本的服务端启动方法
//方法的注册名必须和对应的方法一一对应
@RpcMethodCluster(method = {"Hello","Bye","GetName","GetPerson"},startNum = {3,3,3,1})
@RpcServerBootStrap(version = "2.2")
@RpcToolsSelector(rpcTool = "Netty")
public class ServerCall {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException, NoSuchMethodException {
        ChosenServerCall.start();
    }
}
