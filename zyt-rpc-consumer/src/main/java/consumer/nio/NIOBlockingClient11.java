package consumer.nio;

import entity.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

//v1.1版本
//阻塞NIO消费端 解决沾包问题
public class NIOBlockingClient11 {
    public static void start(String HostName, int PORT) throws IOException {
        start0(HostName,PORT);
    }

    //真正启动在这
    private static void start0(String hostName, int port) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        System.out.println("-----------服务消费方启动-------------");
        //设置阻塞
        socketChannel.configureBlocking(true);
        //建立链接  阻塞连接  但我们是要等他连接上
       socketChannel.connect(new InetSocketAddress(hostName,port));

        //真正的业务逻辑  等待键盘上的输入 进行发送信息
        Scanner scanner = new Scanner(System.in);

        //输入输出通道都放在外面

        ObjectOutputStream outputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
        //都是阻塞等待 发完了 接收完了 才能进行下一步 不然会报异常
        while (true)
        {
            int methodNum = scanner.nextInt();
            String message = scanner.next();
            RpcRequest request = new RpcRequest(methodNum,message);
            //进行修订 使得可以传送对象 通过自带的io流进行 避免出现沾包拆包现象

            outputStream.writeObject(request);
            System.out.println("消息发送");
            try {
                String msg = (String)objectInputStream.readObject();
                System.out.println("收到来自客户端的消息"+msg);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}


