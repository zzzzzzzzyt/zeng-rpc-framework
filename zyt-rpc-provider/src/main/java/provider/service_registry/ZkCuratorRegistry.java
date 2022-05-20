package provider.service_registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.nio.charset.StandardCharsets;

import static constants.RpcConstants.ZOOKEEPER_ADDRESS;


//通过curator简化 zookeeper对相应的服务端服务注册的流程 更轻松的看懂

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class ZkCuratorRegistry {
    public static void registerMethod(String rpcServiceName, String hostName, int port) {
        // 创建连接 然后将对应的对象注册进去即可
        // BackoffRetry 退避策略，决定失败后如何确定补偿值。
        // ExponentialBackOffPolicy
        // 指数退避策略，需设置参数sleeper、initialInterval、
        // maxInterval和multiplier，initialInterval指定初始休眠时间，默认100毫秒，
        // maxInterval指定最大休眠时间，默认30秒，multiplier指定乘数，即下一次休眠时间为当前休眠时间*multiplier；

        CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_ADDRESS,
                new ExponentialBackoffRetry(1000, 3));
        //需要启动  当注册完毕后记得关闭 不然会浪费系统资源
        client.start();

        //进行创建  首先先判断是否创建过service还有对应的方法路径 是这样判断 但是我可能还没有完全玩透curator
        //多线程问题 一定要进行加锁
        synchronized (ZkCuratorRegistry.class) {
            try {
                if (client.checkExists().forPath("/service") == null) {
                    client.create().forPath("/service", "".getBytes(StandardCharsets.UTF_8));
                }
                if (client.checkExists().forPath("/service/" + rpcServiceName) == null) {
                    client.create().forPath("/service/" + rpcServiceName, "".getBytes(StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        String date = hostName + ":" + port;
        try {
            client.create().forPath("/service/" + rpcServiceName + "/" + date, "0".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        client.close();
        log.info("服务端:" + hostName + ":" + port + ":" + rpcServiceName + "方法在zkCurator中注册完毕");
    }
}
