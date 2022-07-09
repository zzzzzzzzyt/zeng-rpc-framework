package provider.service_registry;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import constants.RpcConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NacosServiceRegistry {
    //直接进行注册
    public static void registerMethod(String rpcServiceName, String hostname, int port) {
        Properties properties = RpcConstants.propertiesInit();
        try {
            //创建namingService
            NamingService namingService = NacosFactory.createNamingService(properties);
            //进行注册
            namingService.registerInstance(rpcServiceName, hostname, port, "DEFAULT");
            log.info("服务端:" + hostname + ":" + port + ":" + rpcServiceName + "方法在nacos中注册完毕");
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
    }
}
