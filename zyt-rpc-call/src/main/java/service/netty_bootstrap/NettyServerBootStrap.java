package service.netty_bootstrap;

import annotation.RpcMethodCluster;
import annotation.RpcServerBootStrap;
import init.ZK;
import org.apache.zookeeper.KeeperException;
import provider.bootstrap.netty.NettyProviderBootStrap20;
import provider.bootstrap.netty.NettyProviderBootStrap21;
import provider.bootstrap.netty.NettyProviderBootStrap22;
import service.ServerCall;

import java.io.IOException;

public class NettyServerBootStrap {
    public static void start() throws InterruptedException, IOException, KeeperException {
        //先对ZK进行初始化
        ZK.init();
        RpcServerBootStrap annotation = ServerCall.class.getAnnotation(RpcServerBootStrap.class);
        //当前服务端启动器 class对象
        String currentServerBootStrapVersion = annotation.version();

        //获取对应的方法和个数 然后进行启动
        //1.获取对应方法 在获取对应的注解  注解中的属性
        RpcMethodCluster nowAnnotation = ServerCall.class.getAnnotation(RpcMethodCluster.class);
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

        //根据对应的启动版本进行启动
        switch (currentServerBootStrapVersion)
        {

            case "2.0": //2.0版本只是进行了测试 简单的实现了远端信息传输
                NettyProviderBootStrap20.main(new String[]{"127.0.0.1",String.valueOf(6668)});
                break;
            case "2.1":
                NettyProviderBootStrap21.main(new String[]{methodBuilder.toString(),numBuilder.toString()});
                break;
            case "2.2": //沿用 就是 做个区分  这个版本时进行序列化的测试
                NettyProviderBootStrap22.main(new String[]{methodBuilder.toString(),numBuilder.toString()});
                break;
            default:
                System.out.println("兄弟，该版本还在脑海中构思，如果你有想法可以pr给我");
        }
    }
}
