package consumer.netty_client_handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import serialization.entity.Person;

import java.util.concurrent.Callable;


//实现了Callable接口实现了异步调用

public class NettyClientHandler22 extends ChannelInboundHandlerAdapter implements Callable{
    //传入的参数
    private Object param;
    private Object response;
    private ChannelHandlerContext context;

    public void setParam(Object param) {
        this.param = param;
    }

    //当成功建立 就赋值上下文对象
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        System.out.println("U•ェ•*U 成功连接");

    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
        notify();
    }

    //调用的时候  就进行传输
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        wait();
        return response;
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
