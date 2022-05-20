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


//v1.0版本非阻塞nio

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NIONonBlockingClient12 {
    public static String start(String hostName, int port, String msg) {
        return start0(hostName, port, msg);
    }

    //真正启动在这
    private static String start0(String hostName, int port, String msg) {
        //得到一个网络通道
        Selector selector = null;
        try {
            SocketChannel socketChannel = SocketChannel.open();
            log.info("-----------服务消费方启动-------------");
            socketChannel.configureBlocking(false);
            //建立链接  非阻塞连接  但我们是要等他连接上
            if (!socketChannel.connect(new InetSocketAddress(hostName, port))) {
                while (!socketChannel.finishConnect()) ;
            }
            //创建选择器 进行监听读事件
            selector = Selector.open();

            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

            //进行发送 发的太快了 来不及收到
            socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        //直接进行监听
        while (true) {
            //捕获异常  监听读事件
            try {
                assert selector != null;
                if (selector.select(1000) == 0) {
                    continue;
                }
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel channel = (SocketChannel) key.channel();
                    int read = 1;
                    //用这个的原因是怕 多线程出现影响
                    StringBuilder stringBuffer = new StringBuilder();
                    while (read != 0) {
                        buffer.clear();
                        read = channel.read(buffer);
                        stringBuffer.append(new String(buffer.array(), 0, read));
                    }
                    return stringBuffer.toString();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}


