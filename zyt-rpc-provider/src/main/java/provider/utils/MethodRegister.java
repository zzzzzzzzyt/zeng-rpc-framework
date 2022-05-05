package provider.utils;

import annotation.RegistryChosen;
import exception.RpcException;
import provider.service_registry.NacosServiceRegistry;
import provider.service_registry.ZkCuratorRegistry;
import provider.service_registry.ZkServiceRegistry;
import register.Register;



//直接实现启动类根据启动类接口上的注解选择对应需要选取的方法
public class MethodRegister  {
    /**
     * 实际进行注册的方法
     * @param method 方法名字
     * @param ip  对应的ip
     * @param port    对应的port
     */
    public static void register(String method, String ip, int port) throws Exception {

        RegistryChosen annotation = Register.class.getAnnotation(RegistryChosen.class);

        switch (annotation.registryName())
        {
            case "nacos":
                NacosServiceRegistry.registerMethod(method,ip, port);
                break;
            case "zookeeper":
                ZkServiceRegistry.registerMethod(method,ip,port);
                break;
            case "zkCurator":
                ZkCuratorRegistry.registerMethod(method,ip,port);
                break;
            default:
                throw new RpcException("不存在该注册中心");
        }
    }
}
