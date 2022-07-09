package provider.test;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import constants.RpcConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Properties;

//进行测试消费

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NacosTest {
    @Test
    public void registryTest() {
        Properties properties = RpcConstants.propertiesInit();
        try {
            //创建namingService
            NamingService namingService = NacosFactory.createNamingService(properties);
            namingService.registerInstance("nacos.test.3", "11.11.11.11", 8888, "DEFAULT");
            namingService.registerInstance("nacos.test.3", "2.2.2.2", 9999, "DEFAULT");
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
    }
}
