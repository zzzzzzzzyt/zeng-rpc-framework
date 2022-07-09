package loadbalance;


import org.apache.zookeeper.ZooKeeper;

/*
    RandomLoadBalance.class 随机均衡策略
    AccessLoadBalance.class 获取次数均衡策略
    ConsistentLoadBalance.class 一致性哈希均衡策略
 */

//实现不同的负载均衡策略

public interface LoadBalance {
    //通过负载均衡策略返回相应地址
    String loadBalance(ZooKeeper zooKeeper, String path);
}
