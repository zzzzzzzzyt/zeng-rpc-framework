import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author 祝英台炸油条
 */
@Slf4j
class ZKServiceTest {

    @Test
    void connect() {
        try {
            String connectString = "zytCentos:2181";
            int sessionTimeout = 2000;
            ZooKeeper zooKeeper = new ZooKeeper(
                    connectString, //连接地址 如果写多个那会
                    sessionTimeout,//即超过该时间后，客户端没有向服务器端发送任何请求（正常情况下客户端会每隔一段时间发送心跳请求，此时服务器端会从新计算客户端的超时时间点的），
                    // 则服务器端认为session超时，清理数据。此时客户端的ZooKeeper对象就不再起作用了，需要再重新new一个新的对象了。
                    null           //监听器 暂时不设置
            );
            if (zooKeeper.exists("/test", null) == null)
                zooKeeper.create("/test", "".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zooKeeper.create("/test/hello", "12".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (IOException | KeeperException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }


    @Test
    void testInterface() {
    }
}


