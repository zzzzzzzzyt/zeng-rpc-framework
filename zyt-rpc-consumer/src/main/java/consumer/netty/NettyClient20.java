package consumer.netty;

import consumer.netty_client_handler.NettyClientHandler20;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


//实际客户端启动类

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyClient20 {
    public static void start(String hostName, int port) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new NettyClientHandler20());
                        }
                    });

            ChannelFuture channelFuture = null;
            try {
                channelFuture = bootstrap.connect(hostName, port).sync();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            //因为上面其实已经是同步  所以下面的监听器可以不用
            assert channelFuture != null;
            channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
                if (channelFuture1.isSuccess()) {
                    log.info("连接" + hostName + ":" + port + "成功");
                } else {
                    log.info("连接" + hostName + ":" + port + "失败");
                }
            });

            //监听关闭事件，本来是异步的，现在转换为同步事件
            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        } finally {
            //优雅的关闭 group
            workGroup.shutdownGracefully();
        }
    }
}
