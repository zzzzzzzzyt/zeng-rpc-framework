package service.call;

import annotation.RpcToolsSelector;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import method.Customer;
import service.ClientCall;
import service.call.netty_call.NettyClientCall;
import service.call.nio_call.NIOClientCall;
import java.io.IOException;

//根据获取对应的启动类注解 来选择启动方法

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class ChosenClientCall {
    public static Customer start(){
        RpcToolsSelector annotation = ClientCall.class.getAnnotation(RpcToolsSelector.class);
        switch (annotation.rpcTool())
        {
            //暂时还没有 return的对象
            case "Netty":
                return NettyClientCall.main(null);
            case "Nio":
                return NIOClientCall.main(null);
            default:
                try {
                    throw new RpcException("暂时还没有该方法，博主正在努力跟进中"); //抛出异常后进行捕获
                } catch (RpcException e) {
                    log.error(e.getMessage(),e);
                }
        }
    }
}
