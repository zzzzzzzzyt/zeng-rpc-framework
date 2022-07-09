package provider.utils;

import annotation.RegistryChosen;
import configuration.GlobalConfiguration;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import provider.service_registry.NacosServiceRegistry;
import provider.service_registry.ZkCuratorRegistry;
import provider.service_registry.ZkServiceRegistry;


//直接实现启动类根据启动类接口上的注解选择对应需要选取的方法

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class MethodRegister {
    /**
     * 实际进行注册的方法
     *
     * @param method 方法名字
     * @param ip     对应的ip
     * @param port   对应的port
     */
    public static void register(String method, String ip, int port) {

        RegistryChosen annotation = GlobalConfiguration.class.getAnnotation(RegistryChosen.class);

        switch (annotation.registryName()) {
            case "nacos":
                NacosServiceRegistry.registerMethod(method, ip, port);
                break;
            case "zookeeper":
                ZkServiceRegistry.registerMethod(method, ip, port);
                break;
            case "zkCurator":
                ZkCuratorRegistry.registerMethod(method, ip, port);
                break;
            default:
                try {
                    throw new RpcException("不存在该注册中心");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                }
        }
    }
}
