package loadbalance;

import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class AccessLoadBalance implements LoadBalance {
    @Override
    public String loadBalance(ZooKeeper zooKeeper, String path) {
        String address = "";
        try {
            List<String> children = zooKeeper.getChildren(path, false, null);
            if (children.isEmpty()) {
                try {
                    throw new RpcException("当前没有服务器提供该服务 请联系工作人员");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                }
            }
            //进行排序 根据每个节点的访问次数 从小到大进行排序  然后选用最小的
            children.sort((o1, o2) -> {

                try {
                    return Integer.parseInt(new String(zooKeeper.getData(path + "/" + o1, false, null)))
                            -
                            Integer.parseInt(new String(zooKeeper.getData(path + "/" + o2, false, null)));
                } catch (KeeperException | InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                return 0;
            });
            //对选用的对象的访问量加1  todo 暂时不知道怎么让数据直接+1
            // 获取节点数据+1，然后修改对应节点，
            String chooseNode = children.get(0);
            byte[] data = zooKeeper.getData(path + "/" + chooseNode, false, null);
            int visitCount = Integer.parseInt(new String(data));
            ++visitCount;
            //version参数用于指定节点的数据版本，表名本次更新操作是针对指定的数据版本进行的。 cas
            zooKeeper.setData(path + "/" + chooseNode, String.valueOf(visitCount).getBytes(StandardCharsets.UTF_8), -1);
            address = children.get(0);
        } catch (KeeperException | InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return address;
    }
}
