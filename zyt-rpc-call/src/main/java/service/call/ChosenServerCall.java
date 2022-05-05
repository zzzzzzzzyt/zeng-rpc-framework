package service.call;


import annotation.RpcToolsSelector;
import org.apache.zookeeper.KeeperException;
import service.ServerCall;
import service.call.netty_call.NettyServerCall;
import service.call.nio_call.NIOServerCall;

import java.io.IOException;

//根据获取对应的启动类注解 来选择启动方法
public class ChosenServerCall {
    public static void start() throws IOException, InterruptedException, KeeperException, NoSuchMethodException {
        RpcToolsSelector annotation = ServerCall.class.getAnnotation(RpcToolsSelector.class);
        switch (annotation.rpcTool())
        {
            case "Netty":
                NettyServerCall.main(null);
                break;
            case "Nio":
                NIOServerCall.main(null);
                break;
            default:
                System.out.println("还没有那个方法呢，要不你写一个给我提个pr，我直接采纳");
        }
    }
}
