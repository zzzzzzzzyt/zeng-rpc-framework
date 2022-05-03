package consumer.servicediscovery;

import annotation.LoadBalanceMethodImpl;
import constants.RpcConstants;
import consumer.nio.NIONonBlockingClient12;
import exception.RpcException;
import loadbalance.LoadBalance;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
    public static String getMethodAddress(String methodName) throws RpcException, InterruptedException, KeeperException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //判断节点中是否存在对应路径  不存在则抛出异常
        if (zooKeeper.exists("/service/"+methodName,null)==null)
        {
            System.out.println("不存在该方法");
            throw new RpcException();
        }

        String prePath = "/service/"+methodName;
        //v1.5修改使用负载均衡策略 根据接口上注解选择的实现类进行调用
        LoadBalanceMethodImpl annotation = LoadBalance.class.getAnnotation(LoadBalanceMethodImpl.class);
        Class methodClass = annotation.chosenMethod();
        Method method = methodClass.getMethod("loadBalance", new Class[]{ZooKeeper.class, String.class});
        //被选中的负载均衡实现类的对象  通过反射执行  获取对应的地址
        Object methodChosenClass = methodClass.newInstance();
        String address = (String) method.invoke(methodChosenClass,zooKeeper,prePath);

        return address;
    }


    public static String getStart(String methodName,String msg) throws IOException, RpcException, InterruptedException, KeeperException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
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
