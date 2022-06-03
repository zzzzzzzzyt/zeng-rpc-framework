package provider.monitor;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import provider.monitor.mapper.RpcMonitorMapper;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/3 19:47
 **/
public class RpcMonitorOperator {

    ApplicationContext context=new ClassPathXmlApplicationContext("spring-mybatis.xml");
    RpcMonitorMapper rpcMonitorMapper=context.getBean(RpcMonitorMapper.class);


    public void deleteAll() {
        rpcMonitorMapper.delete(null);
    }
}
