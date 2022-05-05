package service.call.nio_call;



import org.apache.zookeeper.KeeperException;
import service.nio_bootstrap.NIOServerBootStrap;

import java.io.IOException;

//通用启动类 将启动的逻辑藏在ServerBootStrap中

public class NIOServerCall {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException, NoSuchMethodException {
        NIOServerBootStrap.start();
    }
}
