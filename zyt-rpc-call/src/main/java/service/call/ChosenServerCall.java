package service.call;


import annotation.RpcToolsSelector;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import service.ServerCall;
import service.call.netty_call.NettyServerCall;
import service.call.nio_call.NIOServerCall;


//根据获取对应的启动类注解 来选择启动方法

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class ChosenServerCall {
    public static void start() {
        RpcToolsSelector annotation = ServerCall.class.getAnnotation(RpcToolsSelector.class);
        switch (annotation.rpcTool()) {
            case "Netty":
                NettyServerCall.main(null);
                break;
            case "Nio":
                NIOServerCall.main(null);
                break;
            default:
                try {
                    throw new RpcException("暂时还没有该方法，博主正在努力跟进中"); //抛出异常后进行捕获
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                }
        }
    }
}
