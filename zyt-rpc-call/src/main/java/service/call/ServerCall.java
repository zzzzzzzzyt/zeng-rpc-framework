package service.call;


import org.apache.zookeeper.KeeperException;
import service.bootstrap.ServerBootStrap;

import java.io.IOException;

//通用启动类 将启动的逻辑藏在ServerBootStrap中
public class ServerCall {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ServerBootStrap.start();
    }
}
