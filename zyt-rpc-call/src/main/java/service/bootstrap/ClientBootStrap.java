package service.bootstrap;

import annotation.RpcClientBootStrap;

import consumer.bootstrap.nio.*;


import java.io.IOException;

//之后启动直接在这边启动根据 在注解中配置对应的版本号  将相应的操作封装到之后的操作中即可  这样很方便 就是每次咱加一个启动器还得改下switch
//比如说这里的version 1.2 就是v1.2版本的启动器
@RpcClientBootStrap(version = "1.5")
public class ClientBootStrap {
    public static void start() throws IOException{
        //获取当前的注解上的版本然后去调用相应的远端方法  反射的方法
        //当前客户端启动器class对象
        Class<ClientBootStrap> currentClientBootStrapClass = ClientBootStrap.class;
        RpcClientBootStrap annotation = currentClientBootStrapClass.getAnnotation(RpcClientBootStrap.class);
        String currentVersion = annotation.version();
        //根据注解获得的版本进行判断是哪个版本 然后进行启动
        switch (currentVersion)
        {
            case "1.0":
                NIOConsumerBootStrap10.main(null);
                break;
            case "1.1":
                NIOConsumerBootStrap11.main(null);
                break;
            case "1.2":
                NIOConsumerBootStrap12.main(null);
                break;
            case "1.4":
                NIOConsumerBootStrap14.main(null);
                break;
            case "1.5":
                NIOConsumerBootStrap15.main(null);
                break;
            default:
                System.out.println("太着急了兄弟，这个版本还没出呢！要不你给我提个PR");
        }
    }
}
