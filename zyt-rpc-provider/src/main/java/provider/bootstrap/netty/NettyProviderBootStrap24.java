package provider.bootstrap.netty;




import lombok.extern.slf4j.Slf4j;
import provider.netty.NettyServer24;


/*
    以netty为网络编程框架的服务提供端启动类
 */
//实现方法和之前都是比较一致的 每个对象要开一个线程去执行呢 原因是我们会启动同步等他们关闭 才出来 这样才能关闭对应的管道
/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyProviderBootStrap24 {
    static volatile int port = 6666; //对应的端口 要传过去 注册到注册中心去
    public static void main(String[] args) {
        //直接在这里将对应的方法什么的进行分开 然后传过去
        String methods = args[0];
        String nums = args[1];
        String[] methodArray = methods.split(",");
        String[] methodNumArray = nums.split(",");
        //进行创建  可能会出问题 这边的端口
        for (int i = 0; i < methodArray.length; i++) {
            String methodName = methodArray[i];
            for (int methodNum = 0; methodNum < Integer.parseInt(methodNumArray[i]); methodNum++) {
                new Thread(() -> NettyServer24.start(methodName,port++)).start();
            }
        }
    }
}
