package provider.nio;

import lombok.extern.slf4j.Slf4j;
import method.ByeService;
import method.HelloService;
import provider.api.ByeServiceImpl;
import provider.api.HelloServiceImpl;

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
public class NIONonBlockingServer10 {

    //启动
    public static void start(int port) {
        start0(port);
    }


    /*
        真正启动的业务逻辑在这
        因为这是简易版 那么先把异常丢出去
     */
    private static void start0(int port) {
        //创建对应的服务器端通道
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            log.info("-----------服务提供方启动-------------");
            //开启一个选择器 将自己要
            selector = Selector.open();

            //绑定端口开启
            serverSocketChannel.bind(new InetSocketAddress(port));

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
                assert selector != null;
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
                        StringBuilder stringBuilder = new StringBuilder();
                        int read = 1;
                        while (read != 0) {
                            //先清空 防止残留
                            buffer.clear();
                            read = socketChannel.read(buffer);
                            //添加的时候  根据读入的数据进行
                            stringBuilder.append(new String(buffer.array(), 0, read));
                        }
                        //方法号和信息中间有个#进行分割
                        String msg = stringBuilder.toString();
                        String[] strings = msg.split("#");
                        if (strings.length < 2) {
                            //当出现传入错误的时候 报异常
                            throw new RuntimeException("传入错误");
                        }
                        String response;
                        if (strings[0].equals("1")) {
                            HelloService helloService = new HelloServiceImpl();
                            response = helloService.sayHello(strings[1]);
                        } else if (strings[0].equals("2")) {
                            ByeService byeService = new ByeServiceImpl();
                            response = byeService.sayBye(strings[1]);
                        } else {
                            //当出现传入错误的时候 报异常
                            throw new RuntimeException("传入错误");
                        }
                        String responseMsg = "收到信息" + strings[1] + "来自" + socketChannel.socket().getRemoteSocketAddress();
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
