package service.netty_bootstrap;

import annotation.RpcClientBootStrap;
import consumer.bootstrap.netty.NettyConsumerBootStrap20;
import consumer.bootstrap.netty.NettyConsumerBootStrap21;
import consumer.bootstrap.netty.NettyConsumerBootStrap22;
import consumer.bootstrap.netty.NettyConsumerBootStrap24;
import exception.RpcException;
import method.Customer;
import service.ClientCall;

//netty客户端的启动类
public class NettyClientBootStrap {
    public static Customer start() throws InterruptedException, RpcException {
        return start0();
    }

    private static Customer start0() throws InterruptedException, RpcException {

        //获取对应的版本号  然后选取对应的版本进行调用
        String currentClientVersion = ClientCall.class.getAnnotation(RpcClientBootStrap.class).version();

        switch (currentClientVersion)
        {
            case "2.0": //2.0就是简单实现远端调用 所以没实现太那个
                NettyConsumerBootStrap20.main(new String[]{"127.0.0.1", String.valueOf(6668)});
                return null;
            case "2.1":
                return NettyConsumerBootStrap21.main(null);
            case "2.2":
                return NettyConsumerBootStrap22.main(null);
            case "2.4": //这个版本各种序列化工具的使用
                return NettyConsumerBootStrap24.main(null);
            default:
                System.out.println("该版本还没出呢，你如果有想法可以私信我，或者提个pr");
                throw new RpcException("出现问题");
        }
    }
}
