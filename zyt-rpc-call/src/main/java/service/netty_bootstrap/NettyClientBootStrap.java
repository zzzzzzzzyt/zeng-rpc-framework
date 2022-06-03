package service.netty_bootstrap;

import annotation.RpcClientBootStrap;
import consumer.bootstrap.netty.NettyConsumerBootStrap20;
import consumer.bootstrap.netty.NettyConsumerBootStrap21;
import consumer.bootstrap.netty.NettyConsumerBootStrap22;
import consumer.bootstrap.netty.NettyConsumerBootStrap24;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import method.Customer;
import service.ClientCall;

//netty客户端的启动类

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyClientBootStrap {
    public static Customer start() {
        return start0();
    }

    private static Customer start0() {

        //获取对应的版本号  然后选取对应的版本进行调用
        String currentClientVersion = ClientCall.class.getAnnotation(RpcClientBootStrap.class).version();

        switch (currentClientVersion) {
            case "2.0": //2.0就是简单实现远端调用 所以没实现太那个
                NettyConsumerBootStrap20.main(new String[]{"127.0.0.1", String.valueOf(6668)});
                return null;
            case "2.1":
                return NettyConsumerBootStrap21.main();
            case "2.2":
                return NettyConsumerBootStrap22.main();
            case "2.4": //这个版本各种序列化工具的使用
            case "2.5":
            case "2.6":
            case "2.7":
            case "2.8":
            case "2.9":
                return NettyConsumerBootStrap24.main();
            default:
                try {
                    throw new RpcException("该版本还没出呢，你如果有想法可以私信我，或者提个pr");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
        }
    }
}
