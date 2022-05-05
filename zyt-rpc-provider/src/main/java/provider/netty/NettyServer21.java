package provider.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import provider.netty_server_handler.NettyServerHandler21;
import provider.utils.MethodRegister;

//进行启动 绑定然后创建对应的 然后注册进注册中心去
public class NettyServer21 {
    public static void start(String methodName,int port) throws Exception {
        //真正的实现逻辑 被封装到下面的方法当中了
        start0(methodName,port);
    }

    private static void start0(String methodName, int port) throws Exception {
        //先将地址进行注册
        MethodRegister.register(methodName,"127.0.0.1",port);

        //开始创建相应的netty服务端
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //创建对应的工作组 用于处理不同的事件
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //进行初始化
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class) //自身实现的通道
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println(socketChannel.remoteAddress()+"连接上了");
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new NettyServerHandler21(methodName));
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            //对连接结果进行监听
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) System.out.println("连接上"+port+"端口");
                    else System.out.println("连接端口失败");
                }
            });

            //等待关闭 同步
            channelFuture.channel().closeFuture().sync();
        } finally {
            //结束了的话  就进行关闭了
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
