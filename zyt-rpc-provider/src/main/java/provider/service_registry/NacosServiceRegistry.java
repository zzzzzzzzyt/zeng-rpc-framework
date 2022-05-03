package provider.service_registry;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import constants.RpcConstants;

import java.util.Properties;

public class NacosServiceRegistry {
    //直接进行注册
    public static void registerMethod(String RpcServiceName,String hostname,int port) throws NacosException {
        Properties properties = RpcConstants.propertiesInit();
        //创建namingService
        NamingService namingService = NacosFactory.createNamingService(properties);
        //进行注册
        namingService.registerInstance(RpcServiceName, hostname, port, "DEFAULT");
        System.out.println("服务端:"+hostname+":"+port+":"+RpcServiceName+"方法在nacos中注册完毕");
    }
}
