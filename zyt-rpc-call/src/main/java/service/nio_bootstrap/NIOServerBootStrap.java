package service.nio_bootstrap;

import annotation.RpcMethodCluster;
import annotation.RpcServerBootStrap;
import init.ZK;
import org.apache.zookeeper.KeeperException;
import provider.bootstrap.nio.*;
import service.nio_call.NIOServerCall;

import java.io.IOException;


//之后启动直接在这边启动根据 在注解中配置对应的版本号  将相应的操作封装到之后的操作中即可
//比如说这里的version 1.2 就是v1.2版本的启动器
@RpcServerBootStrap(version = "1.5")
public class NIOServerBootStrap {



    public static void start() throws IOException, InterruptedException, KeeperException, NoSuchMethodException {

        //先对ZK进行初始化
        ZK.init();
        Class<NIOServerBootStrap> serverBootStrapClass = NIOServerBootStrap.class;
        RpcServerBootStrap annotation = serverBootStrapClass.getAnnotation(RpcServerBootStrap.class);
        //当前服务端启动器 class对象
        String currentServerBootStrapVersion = annotation.version();

        //获取对应的方法和个数 然后进行启动
        //1.获取对应方法 在获取对应的注解  注解中的属性
        RpcMethodCluster nowAnnotation = NIOServerCall.class.getAnnotation(RpcMethodCluster.class);
        String[] methods = nowAnnotation.method();
        int[] startNums = nowAnnotation.startNum();
        //如果不存在那就返回
        if (methods.length==0)return;
        //2.需要组合在一起传过去  如果不组合分别传 我怕就是端口号会出现问题
        StringBuilder methodBuilder = new StringBuilder();
        StringBuilder numBuilder = new StringBuilder();
        for (String method : methods) {
            methodBuilder.append(method);
            methodBuilder.append(",");
        }
        methodBuilder.deleteCharAt(methodBuilder.length()-1);
        for (int startNum : startNums) {
            numBuilder.append(startNum);
            numBuilder.append(",");
        }
        numBuilder.deleteCharAt(numBuilder.length()-1);

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
            case "1.4":
                //1.4 增加了注册中心 zk
                NIOProviderBootStrap14.main(new String[]{methodBuilder.toString(), numBuilder.toString()});
                break;
            case "1.5":
                //1.5 将注册中心换成了nacos
                NIOProviderBootStrap15.main(new String[]{methodBuilder.toString(), numBuilder.toString()});
                break;
            default:
                System.out.println("太着急了兄弟，这个版本还没出呢！要不你给我提个PR");
        }
    }
}