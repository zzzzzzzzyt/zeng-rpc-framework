package consumer.netty;


import codec.AddCodec;
import consumer.netty_client_handler.NettyClientHandler22;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//实际客户端启动类  进行操作
//不确定能返回什么 所以判断是对象
/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyClient22 {

    //线程池 实现异步调用
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static NettyClientHandler22 clientHandler;

    public static void initClient(String hostName,int port,Method method)
    {

        clientHandler = new NettyClientHandler22();
        //建立客户端监听
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            //加编解码器的逻辑，根据对应的注解进行编码器的添加 这里面有实现对应的逻辑 //
                            AddCodec.addCodec(pipeline,method,true);
                            pipeline.addLast(clientHandler);
                        }
                    });

            //进行连接
            bootstrap.connect(hostName, port).sync();

        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }

    public static Object callMethod(String hostName, int port, Object param, Method method) {

        //我是有多个地方进行调用的 不能只连接一个
        initClient(hostName,port,method);
        clientHandler.setParam(param);
        //接下来这就有关系到调用 直接调用
        try {
            return executor.submit(clientHandler).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
