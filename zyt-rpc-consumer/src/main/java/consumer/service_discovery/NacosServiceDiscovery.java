package consumer.service_discovery;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import constants.RpcConstants;
import consumer.nio.NIONonBlockingClient12;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NacosServiceDiscovery {
    public static String getMethodAddress(String methodName)  {
        Properties properties = RpcConstants.propertiesInit();
        Instance instance = null;
        try {
            NamingService namingService = NacosFactory.createNamingService(properties);

            //这个方法内部实现了负载均衡
            instance = namingService.selectOneHealthyInstance(methodName);
        } catch (NacosException e) {
            log.error(e.getMessage(),e);
        }
        if (instance==null)
        {
            log.info("没有提供该方法");
            try {
                throw new RpcException("没有对应的方法");
            } catch (RpcException e) {
                log.error(e.getMessage(),e);
            }
        }
        //为空的化 就抛出异常
        assert instance != null;
        String ip = instance.getIp();
        int port = instance.getPort();
        return ip+":"+port;
    }

    public static String getStart(String methodName,String msg) {
        //获取相应的远端地址
        String methodAddress = getMethodAddress(methodName);
        //进行连接
        String[] strings = methodAddress.split(":");
        //启动
        String address = strings[0];
        int port = Integer.parseInt(strings[1]);
        return NIONonBlockingClient12.start(address,port,msg);
    }
}
