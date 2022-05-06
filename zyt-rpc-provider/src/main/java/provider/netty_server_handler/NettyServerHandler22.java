package provider.netty_server_handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

//实现简单的服务注册和回写
public class NettyServerHandler22 extends ChannelInboundHandlerAdapter {
    private String methodName;

    //要传入对应的方法名 否则不知道 netty服务器能执行什么方法
    public NettyServerHandler22(String methodName)
    {
        this.methodName = methodName;
    }

    //实现对应的方法

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到来自"+ctx.channel().remoteAddress()+"的信息");
        //使用反射的方法获取对应的类 通过反射再进行执行

        Class<?> calledClass = Class.forName("provider.api."+methodName + "ServiceImpl");
        Method method = calledClass.getMethods()[0];
        Object instance = calledClass.newInstance();
        Object response = method.invoke(instance, msg);
        //获得对应信息并进行回传
        ctx.writeAndFlush(response);
    }

    //出现异常的话 如何进行处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }
}
