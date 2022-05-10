package consumer.netty;


import annotation.HeartBeatTool;
import codec.AddCodec;
import consumer.netty_client_handler.NettyClientHandler24;
import heartbeat.HeartBeat;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//2.4版本主要进行大量序列化工具的集合 然后进行方便式的序列化和反序列化

//实际客户端启动类  进行操作
//不确定能返回什么 所以判断是对象
public class NettyClient24 {

    //线程池 实现异步调用
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static HeartBeatTool heartBeatToolAnnotation = HeartBeat.class.getAnnotation(HeartBeatTool.class);
    // static NettyClientHandler24 clientHandler;//跟他没关系 因为每次都新建一个
    private static final ThreadLocal<NettyClientHandler24> nettyClientHandlerThreadLocal = ThreadLocal.withInitial(()->new NettyClientHandler24());

    public static Object callMethod(String hostName, int port, Object param, Method method) throws Exception {

        NettyClientHandler24 clientHandler = nettyClientHandlerThreadLocal.get();
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

                            /*
                                v2.5更新添加读写空闲处理器
                                IdleStateHandler是netty提供的处理读写空闲的处理器
                                readerIdleTimeSeconds  多长时间没有读 就会传递一个心跳包进行检测
                                writerIdleTimeSeconds  多长时间没有写 就会传递一个心跳包进行检测
                                allIdleTimeSeconds     多长时间没有读写 就会传递一个心跳包进行检测
                                当事件触发后会传递给下一个处理器进行处理，只需要在下一个处理器中实现userEventTriggered事件即可
                             */
                            //时间和实不实现 根据注解 判断是否开启
                            //记住后面一定是要有一个处理器 用来处理触发事件
                            if (heartBeatToolAnnotation.isOpenFunction())
                            {
                                pipeline.addLast(new IdleStateHandler(
                                        heartBeatToolAnnotation.readerIdleTimeSeconds(),
                                        heartBeatToolAnnotation.writerIdleTimeSeconds(),
                                        heartBeatToolAnnotation.allIdleTimeSeconds())
                                );
                            }
                            pipeline.addLast(clientHandler);
                        }
                    });

            //进行连接
            bootstrap.connect(hostName, port).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //我是有多个地方进行调用的 不能只连接一个
        // initClient(hostName,port,method);
        clientHandler.setParam(param);
        clientHandler.setMethod(method);
        //接下来这就有关系到调用 直接调用
        Object response = executor.submit(clientHandler).get();
        nettyClientHandlerThreadLocal.remove(); //一个handler不能加到两个管道中 你说是吧
        return response;

    }
}
