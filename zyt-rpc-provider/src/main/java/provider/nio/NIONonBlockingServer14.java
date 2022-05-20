package provider.nio;

import lombok.extern.slf4j.Slf4j;
import provider.service_registry.ZkServiceRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

//v1.0版本非阻塞服务器端

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NIONonBlockingServer14 {

    //启动
    public static void start(String method, int port) {
        start0(method, port);
    }


    /*
        真正启动的业务逻辑在这
        因为这是简易版 那么先把异常丢出去
     */
    private static void start0(String method, int port) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            //创建对应的服务器端通道
            serverSocketChannel = ServerSocketChannel.open();
            log.info("-----------服务提供方启动-------------");
            //开启一个选择器 将自己要
            selector = Selector.open();

            //绑定端口开启
            serverSocketChannel.bind(new InetSocketAddress(port));

            //将服务注册进zk中
            ZkServiceRegistry.registerMethod(method, "127.0.0.1", port);

            //这里注意 要设置非阻塞   阻塞的话  他会一直等待事件或者是异常抛出的时候才会继续 会浪费cpu
            serverSocketChannel.configureBlocking(false);

            //要先设置非阻塞 再注册  如果时先注册再设置非阻塞会报错
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        //真正的业务逻辑 就是下面
        //循环等待客户端的连接和检查事件的发生
        while (true) {
            //1秒钟无事发生的话  就继续
            try {
                if (selector.select(1000) == 0) {
                    continue;
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

            //获取所有的对象
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    try {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        log.info("连接到消费端" + socketChannel.socket().getRemoteSocketAddress());
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if (key.isReadable()) {
                    //对可能是因为连接下线而触发的事件进行处理
                    try {
                        //反向获取管道
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        //进行调用方法并返回
                        //获得信息
                        StringBuilder stringBuffer = new StringBuilder();
                        int read = 1;
                        while (read != 0) {
                            //先清空 防止残留
                            buffer.clear();
                            read = socketChannel.read(buffer);
                            //添加的时候  根据读入的数据进行
                            stringBuffer.append(new String(buffer.array(), 0, read));
                        }

                        String msg = stringBuffer.toString();

                        //这里要有新逻辑了 根据获得的方法名 去找到相应的方法
                        //方法我们保存在固定位置 同时含有固定后缀
                        String className = method + "ServiceImpl";
                        Class<?> methodClass = Class.forName("provider.api." + className);
                        //实例 要获取对应的实例 或者子对象才能进行反射执行方法
                        Object instance = methodClass.newInstance();

                        //要传入参数的类型
                        String response = (String) methodClass.
                                getMethod("say" + method, String.class).
                                invoke(instance, msg);
                        String responseMsg = "收到信息" + msg + "来自" + socketChannel.socket().getRemoteSocketAddress();
                        log.info(responseMsg);
                        //将调用方法后获得的信息回显
                        ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
                        //写回信息
                        socketChannel.write(responseBuffer);
                    } catch (Exception e) {
                        //进行关闭 并继续执行  取消键的注册 还有关闭管道
                        SocketChannel unConnectChannel = (SocketChannel) key.channel();
                        log.info(((unConnectChannel.socket().getRemoteSocketAddress()) + "下线了"));
                        key.cancel();
                    }
                }
                keyIterator.remove();
            }
        }
    }
}
