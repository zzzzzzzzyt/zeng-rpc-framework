package consumer.zkService;

import constants.RpcConstants;
import consumer.nio.NIONonBlockingClient12;
import exception.RpcException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ZkServiceDiscovery {
    private static String connectString = RpcConstants.ZOOKEEPER_ADDRESS;
    private static int sessionTimeout = RpcConstants.ZOOKEEPER_SESSION_TIMEOUT;
    private static ZooKeeper zooKeeper;

    //第一步当然是连接到远端服务器上了
    public static void getConnect() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    // 根据所请求的服务地址 获取对应的远端地址
    public static String getMethodAddress(String methodName) throws RpcException, InterruptedException, KeeperException {

        //判断节点中是否存在对应路径  不存在则抛出异常
        if (zooKeeper.exists("/service/"+methodName,null)==null)
        {
            System.out.println("不存在该方法");
            throw new RpcException();
        }

        String prePath = "/service/"+methodName;
        //v1.3更新 使用软负载
        //到对应节点中获取下面的子节点
        List<String> children = zooKeeper.getChildren(prePath, false, null);
        if (children.isEmpty())
        {
            System.out.println("当前没有服务器提供该服务 请联系工作人员");
        }

        //进行排序 根据每个节点的访问次数 从小到大进行排序  然后选用最小的
        Collections.sort(children, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                try {
                    return Integer.valueOf(new String(zooKeeper.getData(prePath+"/"+o1,false,null)))
                            -
                            Integer.valueOf(new String(zooKeeper.getData(prePath+"/"+o2,false,null)));
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
        byte[] data = zooKeeper.getData(prePath+"/"+chooseNode, false, null);
        int visitCount = Integer.valueOf(new String(data));
        ++visitCount;
        //version参数用于指定节点的数据版本，表名本次更新操作是针对指定的数据版本进行的。 cas
        zooKeeper.setData(prePath+"/"+chooseNode,String.valueOf(visitCount).getBytes(StandardCharsets.UTF_8),-1);
        String address = new String(children.get(0));
        return address;
    }


    public static String getStart(String methodName,String msg) throws IOException, RpcException, InterruptedException, KeeperException {
        //先进行连接
        getConnect();
        //获取相应的远端地址
        String methodAddress = getMethodAddress(methodName);
        //进行连接
        String[] strings = methodAddress.split(":");
        //启动
        String address = strings[0];
        int port = Integer.valueOf(strings[1]);
        return NIONonBlockingClient12.start(address,port,msg);
    }
}
