package consumer.proxy;

import annotation.RegistryChosen;
import consumer.netty.NettyClient;
import consumer.netty.NettyClient21;
import consumer.service_discovery.NacosServiceDiscovery;
import consumer.service_discovery.ZkCuratorDiscovery;
import consumer.service_discovery.ZkServiceDiscovery;
import exception.RpcException;
import register.Register;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//就是获取代理类 通过代理类中的方法进行对应方法的执行和获取
public class RpcNettyClientProxy {
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //参数 就是我要对其生成代理类的类
    public static Object getBean(final Class<?> serviceClass)
    {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass},
                new InvocationHandler() {
            //根据对应的方法 代理类进行处理
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                Object param = args[0];
                //获取对应的方法地址
                String methodAddress = getMethodAddress(methodName);
                String[] strings = methodAddress.split(":");
                String hostName = strings[0];
                int port = Integer.valueOf(strings[1]);
                //进行方法的调用  随即进行方法的调用
                return NettyClient.callMethod(hostName,port,param,method);
            }
        });
    }

    /**
     * 实际去获得对应的服务 并完成方法调用的方法
     * @param methodName  根据方法名  根据添加的注册中心注解来选择相应的注册中心进行  实现负载均衡获取一个方法对应地址
     * @param
     * @return
     */
    private static String getMethodAddress(String methodName) throws Exception {
        //根据注解进行方法调用
        //根据在代理类上的注解调用  看清楚底下的因为是个class数组 可以直接继续获取 注解
        RegistryChosen annotation = Register.class.getAnnotation(RegistryChosen.class);
        switch (annotation.registryName())
        {
            case "nacos":
                return NacosServiceDiscovery.getMethodAddress(methodName);
            case "zookeeper":
                return ZkServiceDiscovery.getMethodAddress(methodName);
            case "zkCurator":
                return ZkCuratorDiscovery.getMethodAddress(methodName);
            default:
                throw new RpcException("不存在该注册中心");
        }
    }
}

