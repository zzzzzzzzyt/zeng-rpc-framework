package loadbalance;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AccessBalance implements LoadBalance{
    @Override
    public String loadBalance(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren(path, false, null);
        if (children.isEmpty())
        {
            System.out.println("当前没有服务器提供该服务 请联系工作人员");
        }
        //进行排序 根据每个节点的访问次数 从小到大进行排序  然后选用最小的
        Collections.sort(children, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                try {
                    return Integer.valueOf(new String(zooKeeper.getData(path+"/"+o1,false,null)))
                            -
                            Integer.valueOf(new String(zooKeeper.getData(path+"/"+o2,false,null)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        //对选用的对象的访问量加1  todo 暂时不知道怎么让数据直接+1
        // 获取节点数据+1，然后修改对应节点，
        String chooseNode = children.get(0);
        byte[] data = zooKeeper.getData(path+"/"+chooseNode, false, null);
        int visitCount = Integer.valueOf(new String(data));
        ++visitCount;
        //version参数用于指定节点的数据版本，表名本次更新操作是针对指定的数据版本进行的。 cas
        zooKeeper.setData(path+"/"+chooseNode,String.valueOf(visitCount).getBytes(StandardCharsets.UTF_8),-1);
        String address = new String(children.get(0));
        return address;
    }
}
