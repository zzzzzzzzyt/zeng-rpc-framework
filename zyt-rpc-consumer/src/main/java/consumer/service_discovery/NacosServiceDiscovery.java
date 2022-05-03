package consumer.service_discovery;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import constants.RpcConstants;
import consumer.nio.NIONonBlockingClient12;
import exception.RpcException;


import java.io.IOException;
import java.util.Properties;

public class NacosServiceDiscovery {
    public static String getMethodAddress(String methodName) throws NacosException, RpcException {
        Properties properties = RpcConstants.propertiesInit();
        NamingService namingService = NacosFactory.createNamingService(properties);

        //这个方法内部实现了负载均衡
        Instance instance = namingService.selectOneHealthyInstance(methodName);
        if (instance==null)
        {
            System.out.println("没有提供该方法");
            throw new RpcException("没有对应的方法");
        }
        String ip = instance.getIp();
        int port = instance.getPort();
        String methodAddress = ip+":"+port;
        return methodAddress;
    }

    public static String getStart(String methodName,String msg) throws IOException, RpcException,NacosException {
        //获取相应的远端地址
        String methodAddress = getMethodAddress(methodName);
        //进行连接
        String[] strings = methodAddress.split(":");
        //启动
        String address = strings[0];
        int port = Integer.valueOf(strings[1]);
        return NIONonBlockingClient12.start(address,port,msg);
    }
}
