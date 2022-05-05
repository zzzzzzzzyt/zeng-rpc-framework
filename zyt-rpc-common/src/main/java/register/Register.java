package register;

import annotation.RegistryChosen;

//注册中心的选择在这配置
//注册中心的选择 启用的是nacos 目前
@RegistryChosen(registryName = "zkCurator")
public interface Register {
}
