package consumer.nio;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

//v1.0版本非阻塞nio
/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NIONonBlockingClient10 {
    public static void start(String hostName, int port) {
        start0(hostName,port);
    }

    //真正启动在这
    private static void start0(String hostName, int port)  {
        //得到一个网络通道
        SocketChannel socketChannel = null;
        Selector selector = null;
        try {
            socketChannel = SocketChannel.open();
            log.info("-----------服务消费方启动-------------");
            socketChannel.configureBlocking(false);
            //建立链接  非阻塞连接  但我们是要等他连接上
            if (!socketChannel.connect(new InetSocketAddress(hostName,port))) {
                while (!socketChannel.finishConnect());
            }
            //创建选择器 进行监听读事件
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        //创建匿名线程进行监听读事件
        Selector finalSelector = selector;
        new Thread(() -> {
            while (true)
            {
                //捕获异常  监听读事件
                try {
                    if (finalSelector.select(1000)==0)
                    {
                        continue;
                    }
                    Iterator<SelectionKey> keyIterator = finalSelector.selectedKeys().iterator();
                    while (keyIterator.hasNext())
                    {
                        SelectionKey key = keyIterator.next();
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        SocketChannel channel = (SocketChannel)key.channel();
                        int read = 1;
                        //用这个的原因是怕 多线程出现影响
                        StringBuffer stringBuffer = new StringBuffer();
                        while (read!=0)
                        {
                            buffer.clear();
                            read = channel.read(buffer);
                            stringBuffer.append(new String(buffer.array(),0,read));
                        }
                        log.info("收到服务端回信"+stringBuffer.toString());
                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }).start();

        //真正的业务逻辑  等待键盘上的输入 进行发送信息
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            int methodNum = scanner.nextInt();
            String message = scanner.next();
            String msg = new String(methodNum+"#"+message);
            try {
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            log.info("消息发送");
        }
    }
}


