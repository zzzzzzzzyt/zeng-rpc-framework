package provider.netty_server_handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//实现简单的服务注册和回写

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyServerHandler21 extends ChannelInboundHandlerAdapter {
    private final String methodName;

    //要传入对应的方法名 否则不知道 netty服务器能执行什么方法
    public NettyServerHandler21(String methodName) {
        this.methodName = methodName;
    }

    //实现对应的方法

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("收到来自" + ctx.channel().remoteAddress() + "的信息");
        //使用反射的方法获取对应的类 通过反射再进行执行
        Object response = null;
        try {
            Class<?> calledClass = Class.forName("provider.api." + methodName + "ServiceImpl");
            Method method = calledClass.getMethod("say" + methodName, String.class);
            Object instance = calledClass.newInstance();
            response = method.invoke(instance, msg.toString());
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        //获得对应信息并进行回传
        ctx.writeAndFlush(response);
    }

    //出现异常的话 如何进行处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
        log.error(cause.getMessage(), cause);
    }
}
