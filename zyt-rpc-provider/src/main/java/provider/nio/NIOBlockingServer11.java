package provider.nio;

import method.ByeService;
import method.HelloService;
import entity.RpcRequest;
import provider.api.ByeServiceImpl;
import provider.api.HelloServiceImpl;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

//v1.1阻塞nio服务器端
//阻塞NIO服务提供端 解决沾包问题
public class NIOBlockingServer11 {
    //启动
    public static void start(int PORT) throws IOException {
        start0(PORT);
    }


    /*
        真正启动的业务逻辑在这
        因为这是简易版 那么先把异常丢出去
     */
    private static void start0(int port) throws IOException {
        //创建对应的服务器端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println("-----------服务提供方启动-------------");

        //阻塞io不需要选择器

        //绑定端口开启
        serverSocketChannel.bind(new InetSocketAddress(port));

        //设置阻塞
        serverSocketChannel.configureBlocking(true);

        //真正的业务逻辑 就是下面
        //循环等待客户端的连接和检查事件的发生
        while (true)
        {
            SocketChannel channel = serverSocketChannel.accept();
            System.out.println("来自"+channel.socket().getRemoteSocketAddress()+"的连接");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //在内部不断的进行监听  io流是阻塞的
                        InputStream inputStream = channel.socket().getInputStream();
                        OutputStream outputStream = channel.socket().getOutputStream();
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                        while (true)
                        {
                            String response;
                            RpcRequest request = (RpcRequest)objectInputStream.readObject();
                            if (request.getMethodNum()==1)
                            {
                                HelloService helloService = new HelloServiceImpl();
                                response = helloService.sayHello(request.getMessage());
                            }
                            else if (request.getMethodNum()==2)
                            {
                                ByeService helloService = new ByeServiceImpl();
                                response = helloService.sayBye(request.getMessage());
                            }
                            else
                            {
                                System.out.println("传入错误");
                                throw new RuntimeException();
                            }
                            System.out.println("收到客户端"+channel.socket().getRemoteSocketAddress()+"的消息"+response);
                            objectOutputStream.writeObject(response);
                        }
                    } catch (Exception e) {
                        System.out.println("channel"+channel.socket().getRemoteSocketAddress()+"断开连接");
                        try {
                            channel.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}

