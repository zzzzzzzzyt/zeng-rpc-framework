package consumer.netty_client_handler;

import annotation.CodecSelector;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import serialization.Serialization;
import serialization.kryo.KryoUtils;
import serialization.protostuff.ProtostuffUtils;

import java.util.concurrent.Callable;


//实现了Callable接口实现了异步调用

public class NettyClientHandler22 extends ChannelInboundHandlerAdapter implements Callable{
    //传入的参数
    private Object param;
    private Object response;
    private ChannelHandlerContext context;
    private boolean isProtostuff;
    private boolean isKryo;

    public void setParam(Object param) {
        this.param = param;
    }

    //当成功建立 就赋值上下文对象
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        System.out.println("U•ェ•*U 成功连接");
        String codecType = Serialization.class.getAnnotation(CodecSelector.class).Codec();
        if (codecType.equals("protostuff"))isProtostuff = true;
        else if (codecType.equals("kryo"))isKryo = true;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //在这要进行解码 获得传回来的信息  如果是遇到下面的msg 那就代表传回来的肯定是个byte[] 根据我们要的方法进行解码
        if (isProtostuff && msg.getClass()!=String.class)
        {
            ProtostuffUtils protostuffUtils = new ProtostuffUtils();
            //反序列化的模板 是根据我传进来的参数进行改变的
            msg = protostuffUtils.deserialize((byte[]) msg,param.getClass());
        }
        if (isKryo && msg.getClass()!=String.class)
        {
            KryoUtils kryoUtils = new KryoUtils();
            //反序列化的模板 是根据我传进来的参数进行改变的
            msg = kryoUtils.deserialize((byte[]) msg,param.getClass());
        }
        response = msg;
        notify();
    }

    //调用的时候  就进行传输
    @Override
    public synchronized Object call() throws Exception {

        //这个变量的目的就是保留原来的param实际参数类型，当返回的时候 可以当作反序列化的模板
        Object request = param;
        //判断是否需要protostuff进行序列化 因为使用这个进行序列话 是我没有相应的解码器
        if (isProtostuff && param.getClass()!=String.class)
        {
            ProtostuffUtils protostuffUtils = new ProtostuffUtils();
            request = protostuffUtils.serialize(param);
        }
        if (isKryo && param.getClass()!=String.class)
        {
            KryoUtils kryoUtils = new KryoUtils();
            request = kryoUtils.serialize(param);
        }

        context.writeAndFlush(request);
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
