package provider.netty_server_handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

//实现简单的服务注册和回写

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyServerHandler20 extends ChannelInboundHandlerAdapter {

    //读取数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //将信息进行读取 直接这样就可以了
        ByteBuf buf = (ByteBuf) msg;
        log.info("客户端发送消息是：" + buf.toString(CharsetUtil.UTF_8));
        log.info("客户端地址：" + ctx.channel().remoteAddress());
    }

    //数据读取完毕  这里已经是用到了byteBuffer
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //读取完毕进行回显  写回并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("success", CharsetUtil.UTF_8));
    }

    //捕获异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //异常处理 首先先将通道的上下文关闭  每个ctx对应的就是handler本身
        ctx.close();
        cause.printStackTrace();
    }
}
