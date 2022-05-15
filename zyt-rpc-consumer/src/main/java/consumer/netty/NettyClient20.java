package consumer.netty;

import consumer.netty_client_handler.NettyClientHandler20;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


//实际客户端启动类
public class NettyClient20 {
    public static void start(String hostName, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new NettyClientHandler20());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(hostName, port).sync();

            //因为上面其实已经是同步  所以下面的监听器可以不用
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("连接"+hostName+":"+port+"成功");
                    }
                    else
                    {
                        System.out.println("连接"+hostName+":"+port+"失败");
                    }
                }
            });

            //监听关闭事件，本来是异步的，现在转换为同步事件
            channelFuture.channel().closeFuture().sync();
        } finally
        {
            //优雅的关闭 group
            workGroup.shutdownGracefully();
        }
    }
}
