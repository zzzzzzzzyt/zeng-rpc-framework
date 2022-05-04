package consumer.proxy;

import annotation.RegistryChosen;
import com.alibaba.nacos.api.exception.NacosException;
import consumer.bootstrap.nio.NIOConsumerBootstrap;
import consumer.service_discovery.NacosServiceDiscovery;
import consumer.service_discovery.ZkCuratorDiscovery;
import consumer.service_discovery.ZkServiceDiscovery;
import exception.RpcException;
import org.apache.zookeeper.KeeperException;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//代理类的实现
public class RpcClientProxy implements NIOConsumerBootstrap{

    //获取代理对象 并返回 当前类别
    public static Object getBean(final Class<?> serviceClass){
        /*
            参数详解
            1、用哪个类加载器去加载对象
            2、动态代理类需要实现的接口 class[]{xxxx.class} 得到的就是对应的类别
            3、动态代理类执行方法的时候需要干的事
         */
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //暂时还没有设置回信这个操作
                        String methodName = method.getName();
                        //String response = ZkServiceDiscovery.getStart(methodName, (String) args[0]);
                        //String response = NacosServiceDiscovery.getStart(methodName, (String) args[0]);
                        //根据注解类进行调用
                        String response = getResponse(methodName,(String) args[0]);
                        return response;
                    }
                }
        );
    }

    /**
     * 实际去获得对应的服务 并完成方法调用的方法
     * @param methodName
     * @param msg 消息
     * @return
     */
    private static String getResponse(String methodName, String msg) throws Exception {
        //根据注解进行方法调用
        //根据在代理类上的注解调用  看清楚底下的因为是个class数组 可以直接继续获取 注解
        Class<NIOConsumerBootstrap>[] interfaces = (Class<NIOConsumerBootstrap>[]) RpcClientProxy.class.getInterfaces();
        RegistryChosen annotation = interfaces[0].getAnnotation(RegistryChosen.class);
        switch (annotation.registryName())
        {
            case "nacos":
                return NacosServiceDiscovery.getStart(methodName, msg);
            case "zookeeper":
                return ZkServiceDiscovery.getStart(methodName,msg);
            case "zkCurator":
                return ZkCuratorDiscovery.getStart(methodName,msg);
            default:
                throw new RpcException("不存在该注册中心");
        }
    }
}
