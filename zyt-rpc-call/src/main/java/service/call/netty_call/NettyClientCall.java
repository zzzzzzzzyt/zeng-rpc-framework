package service.call.netty_call;

import method.Customer;
import service.netty_bootstrap.NettyClientBootStrap;

//客户端启动类
/**
 * @author 祝英台炸油条
 */
public class NettyClientCall {
    public static Customer main(String[] args)  {
        return NettyClientBootStrap.start();
    }
}
