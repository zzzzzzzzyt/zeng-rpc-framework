package consumer.netty_client_handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyClientHandler20 extends ChannelInboundHandlerAdapter {

    //通道就绪就会发的信息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，服务端", CharsetUtil.UTF_8));
    }

    //这个是收到信息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        log.info("收到来自" + ctx.channel().remoteAddress() + "的消息" + buf.toString(CharsetUtil.UTF_8));
    }
}
