package loadbalance;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance{
    @Override
    public String loadBalance(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        System.out.println(zooKeeper.hashCode());
        List<String> children = zooKeeper.getChildren(path, null,null);
        if (children.isEmpty())
        {
            System.out.println("当前没有服务器提供该服务 请联系工作人员");
        }
        int size = children.size();
        Random random = new Random();
        //这是应该处于0——size-1之间
        int randomIndex = random.nextInt(size);
        String chooseNode = children.get(randomIndex);
        byte[] data = zooKeeper.getData(path + "/" + chooseNode, null, null);
        int visitedCount = Integer.valueOf(new String(data));
        ++visitedCount;
        zooKeeper.setData(path+"/"+ chooseNode, String.valueOf(visitedCount).getBytes(StandardCharsets.UTF_8),-1);
        return chooseNode;
    }
}
