package provider.test;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import constants.RpcConstants;

import org.junit.jupiter.api.Test;


import java.util.Properties;

//进行测试消费
public class NacosTest {
    @Test
    public void registryTest() throws NacosException {
        Properties properties = RpcConstants.propertiesInit();
        //创建namingService
        NamingService namingService = NacosFactory.createNamingService(properties);
        namingService.registerInstance("nacos.test.3", "11.11.11.11", 8888, "DEFAULT");
        namingService.registerInstance("nacos.test.3", "2.2.2.2", 9999, "DEFAULT");
        log.info(namingService.getAllInstances("nacos.test.3"));
    }
}
