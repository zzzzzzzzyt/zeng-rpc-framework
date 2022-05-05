package service.call.netty_call;

import exception.RpcException;
import method.Customer;
import service.netty_bootstrap.NettyClientBootStrap;

//客户端启动类
public class NettyClientCall {
    public static Customer main(String[] args) throws InterruptedException, RpcException {
        return NettyClientBootStrap.start();
    }
}
