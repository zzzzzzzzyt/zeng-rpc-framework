package service.bootstrap;

import annotation.RpcServerBootStrap;
import org.apache.zookeeper.KeeperException;
import provider.bootstrap.NIOProviderBootStrap10;
import provider.bootstrap.NIOProviderBootStrap11;
import provider.bootstrap.NIOProviderBootStrap12;

import java.io.IOException;

//之后启动直接在这边启动根据 在注解中配置对应的版本号  将相应的操作封装到之后的操作中即可
//比如说这里的version 1.2 就是v1.2版本的启动器
@RpcServerBootStrap(version = "1.5")
public class ServerBootStrap {
    public static void start() throws IOException, InterruptedException, KeeperException {
        Class<ServerBootStrap> serverBootStrapClass = ServerBootStrap.class;
        RpcServerBootStrap annotation = serverBootStrapClass.getAnnotation(RpcServerBootStrap.class);
        //当前服务端启动器 class对象
        String currentServerBootStrapVersion = annotation.version();
        switch (currentServerBootStrapVersion)
        {
            case "1.0":
                NIOProviderBootStrap10.main(null);
                break;
            case "1.1":
                NIOProviderBootStrap11.main(null);
                break;
            case "1.2":
                NIOProviderBootStrap12.main(null);
                break;
            default:
                System.out.println("太着急了兄弟，这个版本还没出呢！要不你给我提个PR");
        }
    }
}
