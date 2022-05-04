package service.nio_call;



import annotation.RpcMethodCluster;
import org.apache.zookeeper.KeeperException;
import service.nio_bootstrap.NIOServerBootStrap;

import java.io.IOException;

//通用启动类 将启动的逻辑藏在ServerBootStrap中
//注解 看你想启动多少个服务和对应的方法
@RpcMethodCluster(method = {"Hello","Bye"},startNum = {2,3})
public class NIOServerCall {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException, NoSuchMethodException {
        NIOServerBootStrap.start();
    }
}
