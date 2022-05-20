package consumer.netty_client_handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;


//实现了Callable接口实现了异步调用

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyClientHandler21 extends ChannelInboundHandlerAdapter implements Callable {
    //传入的参数
    private Object param;
    private Object response;
    private ChannelHandlerContext context;

    public void setParam(Object param) {
        this.param = param;
    }

    //当成功建立 就赋值上下文对象
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        context = ctx;
        log.info("U•ェ•*U 成功连接");
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        response = msg;
        notifyAll();
    }

    //调用的时候  就进行传输
    @Override
    public synchronized Object call() {
        context.writeAndFlush(param);
        try {
            wait();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
