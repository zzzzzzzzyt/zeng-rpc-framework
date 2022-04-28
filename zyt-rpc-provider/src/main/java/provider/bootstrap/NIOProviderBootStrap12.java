package provider.bootstrap;


import org.apache.zookeeper.KeeperException;
import provider.nio.NIONonBlockingServer12bye;
import provider.nio.NIONonBlockingServer12hello;

import java.io.IOException;

/*
    以nio为网络编程框架的服务提供端启动类  加入了zk
 */
public class NIOProviderBootStrap12 {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        //启动
        new Thread(new Runnable() {
            @Override
            public void run() {
                //因为每个服务提供端内部都是在监听循环阻塞 每个开启一个线程进行监听
                try {
                    NIONonBlockingServer12hello.start(6666);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //启动
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    NIONonBlockingServer12bye.start(6667);
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
