package provider.utils;

import annotation.RegistryChosen;
import com.alibaba.nacos.api.exception.NacosException;
import exception.RpcException;
import org.apache.zookeeper.KeeperException;
import provider.bootstrap.nio.NIOProviderBootStrap;
import provider.service_registry.NacosServiceRegistry;
import provider.service_registry.ZkServiceRegistry;

import java.io.IOException;

//直接实现启动类根据启动类接口上的注解选择对应需要选取的方法
public class MethodRegister implements NIOProviderBootStrap {
    /**
     * 实际进行注册的方法
     * @param method 方法名字
     * @param ip  对应的ip
     * @param port    对应的port
     */
    public static void register(String method, String ip, int port) throws NacosException, RpcException, IOException, InterruptedException, KeeperException {

        RegistryChosen annotation = MethodRegister.class.getInterfaces()[0].getAnnotation(RegistryChosen.class);

        switch (annotation.registryName())
        {
            case "nacos":
                NacosServiceRegistry.registerMethod(method,ip, port);
                break;
            case "zookeeper":
                ZkServiceRegistry.registerMethod(method,ip,port);
                break;
            default:
                throw new RpcException("不存在该注册中心");
        }
    }
}
