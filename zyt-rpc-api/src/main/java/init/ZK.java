package init;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

import static constants.RpcConstants.ZOOKEEPER_ADDRESS;
import static constants.RpcConstants.ZOOKEEPER_SESSION_TIMEOUT;

//zookeeper 进行一键初始化的方法

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class ZK {

    private static ZooKeeper zooKeeper;

    public static void init() {
        try {
            zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, ZOOKEEPER_SESSION_TIMEOUT, watchedEvent -> {
            });

            //如果存在就删  不存在就不删
            if (zooKeeper.exists("/service", false) != null) {
                //内部得实现递归删除
                deleteAll("/service");
            }
            zooKeeper.close();
        } catch (IOException | KeeperException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    //实现循环递归删除的方法
    private static void deleteAll(String prePath) {
        try {
            List<String> children = zooKeeper.getChildren(prePath, false);
            if (!children.isEmpty()) {
                for (String child : children) {
                    deleteAll(prePath + "/" + child);
                }
            }
            zooKeeper.delete(prePath, -1);
        } catch (KeeperException | InterruptedException e) {
            log.error(e.getMessage(), e); //将错误的信息要输出 而不是丢出去 给最外面进行处理
        }
    }
}
