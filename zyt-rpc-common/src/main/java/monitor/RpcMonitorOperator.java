package monitor;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import monitor.mapper.RpcMonitorMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author 祝英台炸油条
 * @Time : 2022/6/3 19:47
 **/
@Slf4j
public class RpcMonitorOperator {

    static RpcMonitorMapper rpcMonitorMapper;

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mybatis.xml");
        rpcMonitorMapper = context.getBean(RpcMonitorMapper.class);
    }


    /**
     * 作用 就是每次开启服务提供商的时候 删除掉所有的对应项目
     */
    public void deleteAll() {
        rpcMonitorMapper.delete(null);
    }

    /**
     * 每次开启服务的时候 就进行添加对应的服务 当然 起始调用次数为0
     */
    public void addServer(RpcMonitor rpcMonitorServer) {
        rpcMonitorMapper.insert(rpcMonitorServer);
    }

    /**
     * 更新相应的服务  将相应的服务调用次数+1  同时update的时候  会自动的更改调用时间  做这个的时候 要上锁 防止线程冲突
     */
    public synchronized void updateServer(String methodAddress) {
        QueryWrapper<RpcMonitor> wrapper = new QueryWrapper<>();
        // 没问题 查询的时候 是跟对应的列名进行比较
        wrapper.eq("method_name", methodAddress);
        RpcMonitor rpcMonitor = rpcMonitorMapper.selectOne(wrapper);
        if (rpcMonitor == null) try {
            throw new RpcException("监控中心出现错误");
        } catch (RpcException e) {
            log.error(e.getMessage(), e);
            return;
        }
        rpcMonitor.setCallNum(rpcMonitor.getCallNum() + 1);
        rpcMonitor.setCallTime(null);
        rpcMonitorMapper.update(rpcMonitor, new QueryWrapper<RpcMonitor>().eq("id", rpcMonitor.getId()));
    }
}
