package service.call.nio_call;


import method.Customer;
import service.nio_bootstrap.NIOClientBootStrap;

//通用启动类 将启动的逻辑藏在ClientBootStrap中
public class NIOClientCall {
    public static Customer main(String[] args) {
        return NIOClientBootStrap.start();
    }
}
