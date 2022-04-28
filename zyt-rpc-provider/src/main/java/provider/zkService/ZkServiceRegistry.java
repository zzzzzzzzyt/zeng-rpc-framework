package provider.zkService;

import constants.RpcConstants;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


//该类将对应服务端的方法和相应的端口和地址，注册到zooKeeper中
public class ZkServiceRegistry {
    private static String connectString = RpcConstants.ZOOKEEPER_ADDRESS;
    private static int sessionTimeout = RpcConstants.ZOOKEEPER_SESSION_TIMEOUT;
    private static ZooKeeper zooKeeper;

    static void createConnect() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
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
        }

        String date = hostname+":"+port;

        //权限目前都设置为全放开   创建方式均为持久化
        zooKeeper.create("/service/"+RpcServiceName,
                date.getBytes(StandardCharsets.UTF_8),
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
        //先创建对应的额zooKeeper连接客户端再进行相应的注册
        createConnect();
        register(RpcServiceName,hostname,port);
        System.out.println("服务端:"+hostname+":"+port+"方法注册完毕");

    }
}
