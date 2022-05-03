package loadbalance;


import annotation.LoadBalanceMethodImpl;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

//实现不同的负载均衡策略
@LoadBalanceMethodImpl(chosenMethod = RandomBalance.class)
public interface LoadBalance {
    //通过负载均衡策略返回相应地址
    String loadBalance(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException;
}
