package provider.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import provider.netty_server_handler.NettyServerHandler20;


//简单实现  主要还是进行了一段回想

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyServer20 {
    public static void start(String hostName, int port) {
        //该方法完成NettyServer的初始化  好好想想看 该怎么完成这个
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class) //自身所实现的通道
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            //每个用户连接上都会进行初始化
                            log.info("客户socketChannel hashcode=" + socketChannel.hashCode());
                            ChannelPipeline pipeline = socketChannel.pipeline();//每个通道都对应一个管道 将处理器往管道里放
                            pipeline.addLast(new NettyServerHandler20());
                        }
                    });

            log.info("服务器 is ready");

            //连接 同步
            ChannelFuture cf = null;
            try {
                cf = serverBootstrap.bind(hostName, port).sync();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            assert cf != null;
            cf.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("监听端口" + port + "成功");
                } else {
                    log.info("监听端口" + port + "失败");
                }
            });

            //对关闭通道进行监听
            try {
                cf.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        } finally {
            //优雅的关闭两个集群
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
