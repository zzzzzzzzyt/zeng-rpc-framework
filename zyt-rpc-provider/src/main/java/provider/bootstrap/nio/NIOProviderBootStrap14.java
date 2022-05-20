package provider.bootstrap.nio;


import provider.nio.NIONonBlockingServer14;

/*
    以nio为网络编程框架的服务提供端启动类  加入了zk
 */

/**
 * @author 祝英台炸油条
 */
public class NIOProviderBootStrap14 {
    static volatile int port = 6666;

    public static void main(String[] args) {
        //对应的方法和对应的方法数量要启动多少 启动的端口不一样 不能写死 首先是
        String methodStr = args[0];
        String numStr = args[1];
        String[] methods = methodStr.split(",");
        String[] nums = numStr.split(",");
        //进行创建  可能会出问题 这边的端口
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i];
            for (int methodNum = 0; methodNum < Integer.parseInt(nums[i]); methodNum++) {
                new Thread(() -> NIONonBlockingServer14.start(methodName, port++)).start();
            }
        }
    }
}
