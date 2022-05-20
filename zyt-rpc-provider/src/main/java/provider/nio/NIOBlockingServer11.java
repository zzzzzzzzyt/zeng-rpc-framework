package provider.nio;

import entity.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import method.ByeService;
import method.HelloService;
import provider.api.ByeServiceImpl;
import provider.api.HelloServiceImpl;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

//v1.1阻塞nio服务器端
//阻塞NIO服务提供端 解决沾包问题

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NIOBlockingServer11 {
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
        try {
            serverSocketChannel = ServerSocketChannel.open();
            log.info("-----------服务提供方启动-------------");

            //阻塞io不需要选择器

            //绑定端口开启
            serverSocketChannel.bind(new InetSocketAddress(port));

            //设置阻塞
            serverSocketChannel.configureBlocking(true);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        //真正的业务逻辑 就是下面
        //循环等待客户端的连接和检查事件的发生
        while (true) {
            SocketChannel channel = null;
            try {
                assert serverSocketChannel != null;
                channel = serverSocketChannel.accept();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            assert channel != null;
            log.info("来自" + channel.socket().getRemoteSocketAddress() + "的连接");
            SocketChannel finalChannel = channel;
            new Thread(() -> {
                try {
                    //在内部不断的进行监听  io流是阻塞的
                    InputStream inputStream = finalChannel.socket().getInputStream();
                    OutputStream outputStream = finalChannel.socket().getOutputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    while (true) {
                        String response;
                        RpcRequest request = (RpcRequest) objectInputStream.readObject();
                        if (request.getMethodNum() == 1) {
                            HelloService helloService = new HelloServiceImpl();
                            response = helloService.sayHello(request.getMessage());
                        } else if (request.getMethodNum() == 2) {
                            ByeService helloService = new ByeServiceImpl();
                            response = helloService.sayBye(request.getMessage());
                        } else {
                            throw new RuntimeException("传入错误");
                        }
                        log.info("收到客户端" + finalChannel.socket().getRemoteSocketAddress() + "的消息" + response);
                        objectOutputStream.writeObject(response);
                    }
                } catch (Exception e) {
                    log.info("channel" + finalChannel.socket().getRemoteSocketAddress() + "断开连接");
                    try {
                        finalChannel.close();
                    } catch (IOException ex) {
                        log.error(e.getMessage(), e);
                    }
                }
            }).start();
        }
    }
}

