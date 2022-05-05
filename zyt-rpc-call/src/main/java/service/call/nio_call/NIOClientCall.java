package service.call.nio_call;


import exception.RpcException;
import method.Customer;
import service.nio_bootstrap.NIOClientBootStrap;

import java.io.IOException;

//通用启动类 将启动的逻辑藏在ClientBootStrap中
public class NIOClientCall {
    public static Customer main(String[] args) throws IOException, RpcException {
        return NIOClientBootStrap.start();
    }
}
