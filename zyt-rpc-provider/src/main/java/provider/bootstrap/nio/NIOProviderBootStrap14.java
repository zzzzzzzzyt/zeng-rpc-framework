package provider.bootstrap.nio;


import org.apache.zookeeper.KeeperException;
import provider.nio.NIONonBlockingServer14;

import java.io.IOException;

/*
    以nio为网络编程框架的服务提供端启动类  加入了zk
 */
public class NIOProviderBootStrap14 {
    static volatile int port = 6666;
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        //对应的方法和对应的方法数量要启动多少 启动的端口不一样 不能写死 首先是
        String methodStr = args[0];
        String numStr = args[1];
        String[] methods = methodStr.split(",");
        String[] nums = numStr.split(",");
        //进行创建  可能会出问题 这边的端口
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i];
            for (Integer methodNum = 0; methodNum < Integer.valueOf(nums[i]); methodNum++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            NIONonBlockingServer14.start(methodName,port++);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
