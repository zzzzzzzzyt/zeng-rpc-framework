package service.call;

import annotation.RpcToolsSelector;
import exception.RpcException;
import method.Customer;
import service.ClientCall;
import service.call.netty_call.NettyClientCall;
import service.call.nio_call.NIOClientCall;

import java.io.IOException;

//根据获取对应的启动类注解 来选择启动方法
public class ChosenClientCall {
    public static Customer start() throws InterruptedException, RpcException, IOException {
        RpcToolsSelector annotation = ClientCall.class.getAnnotation(RpcToolsSelector.class);
        switch (annotation.rpcTool())
        {
            //暂时还没有 return的对象
            case "Netty":
                return NettyClientCall.main(null);
            case "Nio":
                return NIOClientCall.main(null);
            default:
                System.out.println("还没有那个方法呢，要不你写一个给我提个pr，我直接采纳");
                throw new RpcException("暂时还没有该方法，博主正在努力跟进中");
        }
    }
}
