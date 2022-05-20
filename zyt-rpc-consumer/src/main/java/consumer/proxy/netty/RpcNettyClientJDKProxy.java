package consumer.proxy.netty;

import annotation.RegistryChosen;
import configuration.GlobalConfiguration;
import consumer.netty.NettyClient;
import consumer.proxy.ClientProxy;
import consumer.service_discovery.NacosServiceDiscovery;
import consumer.service_discovery.ZkCuratorDiscovery;
import consumer.service_discovery.ZkServiceDiscovery;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;


//就是获取代理类 通过代理类中的方法进行对应方法的执行和获取

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class RpcNettyClientJDKProxy implements ClientProxy {
    //参数 就是我要对其生成代理类的类
    public Object getBean(final Class<?> serviceClass) {
        //根据对应的方法 代理类进行处理
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass},
                (proxy, method, args) -> {
                    String methodName = method.getName();
                    Object param = args[0];
                    //获取对应的方法地址
                    String methodAddress = null;
                    try {
                        methodAddress = getMethodAddress(methodName);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                    assert methodAddress != null;
                    String[] strings = methodAddress.split(":");
                    String hostName = strings[0];
                    int port = Integer.parseInt(strings[1]);
                    //进行方法的调用  随即进行方法的调用
                    return NettyClient.callMethod(hostName, port, param, method);
                });
    }

    /**
     * 实际去获得对应的服务 并完成方法调用的方法
     *
     * @param methodName 根据方法名  根据添加的注册中心注解来选择相应的注册中心进行  实现负载均衡获取一个方法对应地址
     */
    private static String getMethodAddress(String methodName) {
        //根据注解进行方法调用
        //根据在代理类上的注解调用  看清楚底下的因为是个class数组 可以直接继续获取 注解
        RegistryChosen annotation = GlobalConfiguration.class.getAnnotation(RegistryChosen.class);
        switch (annotation.registryName()) {
            case "nacos":
                return NacosServiceDiscovery.getMethodAddress(methodName);
            case "zookeeper":
                return ZkServiceDiscovery.getMethodAddress(methodName);
            case "zkCurator":
                return ZkCuratorDiscovery.getMethodAddress(methodName);
            default:
                try {
                    throw new RpcException("不存在该注册中心");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
        }
    }
}

