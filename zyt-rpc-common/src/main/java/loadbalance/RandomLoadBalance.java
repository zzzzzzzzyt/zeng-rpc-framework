package loadbalance;

import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class RandomLoadBalance implements LoadBalance {
    @Override
    public String loadBalance(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren(path, null, null);
        if (children.isEmpty()) {
            try {
                throw new RpcException("当前没有服务器提供该服务 请联系工作人员");
            } catch (RpcException e) {
                log.error(e.getMessage(), e);
            }
        }
        int size = children.size();
        Random random = new Random();
        //这是应该处于0——size-1之间
        int randomIndex = random.nextInt(size);
        String chooseNode = children.get(randomIndex);
        byte[] data = zooKeeper.getData(path + "/" + chooseNode, null, null);
        int visitedCount = Integer.parseInt(new String(data));
        //这个加了  就是让我们看的明显  随机性
        ++visitedCount;
        zooKeeper.setData(path + "/" + chooseNode, String.valueOf(visitedCount).getBytes(StandardCharsets.UTF_8), -1);
        return chooseNode;
    }
}
