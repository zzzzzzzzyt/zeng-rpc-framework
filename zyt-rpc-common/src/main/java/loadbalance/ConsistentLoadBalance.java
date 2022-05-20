package loadbalance;

import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

//一致性哈希 进行负载均衡计算
/**
 * @author 祝英台炸油条
 */
@Slf4j
public class ConsistentLoadBalance implements LoadBalance{
    @Override
    public String loadBalance(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren(path, false, null);
        if (children.isEmpty())
        {
            try {
                throw new RpcException("当前没有服务器提供该服务 请联系工作人员");
            } catch (RpcException e) {
                log.error(e.getMessage(),e);
            }
        }
        //我这个属于的是简单版的一致性哈希
        int zkHashCode = zooKeeper.hashCode();
        return children.get(zkHashCode%children.size());

        //细致版一致性哈希 太麻烦了
        /*
        HashMap<Integer,String> hashAddress = new HashMap();
        for (String child : children) {
            hashAddress.put(child.hashCode()%,child);
        }
        //先对对应的获得的子节点进行地址的储存
        //我这个属于的是简单版的一致性哈希
        int zkHashCode = zooKeeper.hashCode();
        long address = (long)(zkHashCode % Math.pow(2, 32));
        while (!hashAddress.containsKey(address))
        {
            if(address == (long)Math.pow(2,32)-1)
            {
                address = 0;
            }
            else  ++address;
        }
        return hashAddress.get(address);
        */
    }
}
