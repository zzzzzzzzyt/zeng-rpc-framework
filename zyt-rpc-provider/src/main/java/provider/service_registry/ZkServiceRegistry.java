package provider.service_registry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static constants.RpcConstants.ZOOKEEPER_ADDRESS;
import static constants.RpcConstants.ZOOKEEPER_SESSION_TIMEOUT;


//该类将对应服务端的方法和相应的端口和地址，注册到zooKeeper中
public class ZkServiceRegistry {
    private static String connectString = ZOOKEEPER_ADDRESS;
    private static int sessionTimeout = ZOOKEEPER_SESSION_TIMEOUT;
    private static ZooKeeper zooKeeper;

    //只需要初始化一次 每次都进行 会消耗资源
    static {
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //创建成功后把方法注册进去
    static void register(String RpcServiceName,String hostname,int port) throws InterruptedException, KeeperException {
        //节点名就是方法名  然后对应的数据就是hostname+"："+port


        //因为这个地区属于一个临界区 可能会发生线程不安全问题 所以进行上🔒
        synchronized (ZkServiceRegistry.class) {
            Stat exists = zooKeeper.exists("/service", false);
            if (exists ==null) {
                zooKeeper.create("/service",
                        "".getBytes(StandardCharsets.UTF_8),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT
                );
            }

            //v1.3进行软负载均衡修改
            exists = zooKeeper.exists("/service/"+RpcServiceName, false);
            if (exists ==null) {
                zooKeeper.create("/service/"+RpcServiceName,
                        "".getBytes(StandardCharsets.UTF_8),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT
                );
            }

        }

        String date = hostname+":"+port;

        //权限目前都设置为全放开   创建方式均为持久化
        //修改 v1.3  数据为访问次数 应该是可以进行加减的  然后发现服务端取的是最低的然后再进行+1
        zooKeeper.create("/service/"+RpcServiceName+"/"+date,
                "0".getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT
        );
    }

    /**
     *
     * @param RpcServiceName 这是对应的服务名
     * @param hostname 和可以调用该服务的ip
     * @param port  还有对应的端口号
     * @throws IOException
     * @throws InterruptedException
     */
    public static void registerMethod(String RpcServiceName,String hostname,int port) throws IOException, InterruptedException, KeeperException {
        register(RpcServiceName,hostname,port);
        log.info("服务端:"+hostname+":"+port+":"+RpcServiceName+"方法在zk中注册完毕");
    }
}
