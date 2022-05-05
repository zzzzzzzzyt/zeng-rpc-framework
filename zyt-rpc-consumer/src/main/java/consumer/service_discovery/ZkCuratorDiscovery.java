package consumer.service_discovery;

import annotation.LoadBalanceMethodImpl;
import constants.RpcConstants;
import consumer.nio.NIONonBlockingClient12;
import exception.RpcException;
import loadbalance.LoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//简化zookeeper的使用  更加方便获取远端的方法的信息
public class ZkCuratorDiscovery {
    private static String connectString = RpcConstants.ZOOKEEPER_ADDRESS;

    //获取对应的地址
    public static String getMethodAddress(String methodName) throws Exception {
        //创建连接后 获取对应的地址 地址和对应的端口信息 传入即可
        //同时还要实现负载均衡
        //BackoffRetry 退避策略，决定失败后如何确定补偿值。
        //ExponentialBackOffPolicy
        //指数退避策略，需设置参数sleeper、initialInterval、
        // maxInterval和multiplier，initialInterval指定初始休眠时间，默认100毫秒，
        // maxInterval指定最大休眠时间，默认30秒，multiplier指定乘数，即下一次休眠时间为当前休眠时间*multiplier；

        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString,
                new ExponentialBackoffRetry(1000, 3));
        //需要启动  当获取完毕后需要关闭相应的客户端
        client.start();
        //首先获取的时候 要负载均衡
        ZooKeeper zooKeeper = client.getZookeeperClient().getZooKeeper();
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
        String methodAddress = (String) method.invoke(methodChosenClass,zooKeeper,prePath);
        client.close();
        return methodAddress;
    }


    public static String getStart(String methodName, String msg) throws Exception {
        String methodAddress = getMethodAddress(methodName);
        String[] strings = methodAddress.split(":");
        //启动
        String address = strings[0];
        int port = Integer.valueOf(strings[1]);
        return NIONonBlockingClient12.start(address,port,msg);
    }
}
