package register;

import annotation.RegistryChosen;

//注册中心的选择在这配置
//注册中心的选择 启用的是nacos 目前
/*
    zookeeper   zk注册中心
    nacos       nacos实现注册中心
    zkCurator   Curator协助操作注册中心
*/

/**
 * @author 祝英台炸油条
 */
@RegistryChosen(registryName = "zookeeper")
public interface Register {
}
