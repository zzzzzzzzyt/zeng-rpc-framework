package init;

import constants.RpcConstants;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

//zookeeper 进行一键初始化的方法
public class ZK {

    private static ZooKeeper zooKeeper;

    public static void init() throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(RpcConstants.ZOOKEEPER_ADDRESS, RpcConstants.ZOOKEEPER_SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });

        //如果存在就删  不存在就不删
        if (zooKeeper.exists("/service",false)!=null)
        {
            //内部得实现递归删除
            deleteAll("/service");
        }
        zooKeeper.close();
    }

    //实现循环递归删除的方法
    private static void deleteAll(String prePath) throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren(prePath, false);
        if (!children.isEmpty())
        {
            for (String child : children) {
                deleteAll(prePath+"/"+child);
            }
        }
        zooKeeper.delete(prePath,-1);
    }
}
