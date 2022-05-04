package provider.service_registry;

import constants.RpcConstants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.nio.charset.StandardCharsets;


//通过curator简化 zookeeper对相应的服务端服务注册的流程 更轻松的看懂
public class ZkCuratorRegistry {
    private static String connectString = RpcConstants.ZOOKEEPER_ADDRESS;
    public static void registerMethod(String RpcServiceName, String hostname, int port) throws Exception {
        //创建连接 然后将对应的对象注册进去即可
        //BackoffRetry 退避策略，决定失败后如何确定补偿值。
        //ExponentialBackOffPolicy
        //指数退避策略，需设置参数sleeper、initialInterval、
        // maxInterval和multiplier，initialInterval指定初始休眠时间，默认100毫秒，
        // maxInterval指定最大休眠时间，默认30秒，multiplier指定乘数，即下一次休眠时间为当前休眠时间*multiplier；

        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString,
                new ExponentialBackoffRetry(1000, 3));
        //需要启动  当注册完毕后记得关闭 不然会浪费系统资源
        client.start();

        //进行创建  首先先判断是否创建过service还有对应的方法路径 是这样判断 但是我可能还没有完全玩透curator
        //多线程问题 一定要进行加锁
        synchronized (ZkCuratorRegistry.class)
        {
            if (client.checkExists().forPath("/service")==null)
            {
                client.create().forPath("/service","".getBytes(StandardCharsets.UTF_8));
            }
            if (client.checkExists().forPath("/service/"+RpcServiceName)==null)
            {
                client.create().forPath("/service/"+RpcServiceName,"".getBytes(StandardCharsets.UTF_8));
            }
        }
        String date = hostname+":"+port;
        client.create().forPath("/service/"+RpcServiceName+"/"+date,"0".getBytes(StandardCharsets.UTF_8));
        client.close();
        System.out.println("服务端:"+hostname+":"+port+":"+RpcServiceName+"方法在zkCurator中注册完毕");
    }
}
