package consumer.netty_client_handler;


import annotation.CompressFunction;
import compress.Compress;
import compress.CompressTypeTool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import serialization.SerializationTool;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;


/*
*   在2.4版本之后我们就暂时淘汰JDK序列化和protoc编译成的类进行处理了
* */
//实现了Callable接口实现了异步调用

public class NettyClientHandler24 extends ChannelInboundHandlerAdapter implements Callable{
    //传入的参数
    private Object param;
    private Method method;
    private Object response;
    private ChannelHandlerContext context;

    //序列化工具
    static SerializationTool serializationTool = new SerializationTool();

    //判断是否进行解压缩
    static boolean openFunction = Compress.class.getAnnotation(CompressFunction.class).isOpenFunction();

    //解压缩工具
    static CompressTypeTool compressTool = new CompressTypeTool();

    public void setParam(Object param) {
        this.param = param;
    }
    public void setMethod(Method method) {
        this.method = method;
    }

    //当成功建立 就赋值上下文对象
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
        System.out.println("U•ェ•*U 成功连接");
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        byte[] msgByteArray = (byte[]) msg;
        // 根据是否解压 首先把收到的信息进行解压再进行反序列化
        if (openFunction)msgByteArray = compressTool.deCompress((byte[]) msg);

        //在这要进行解码 获得传回来的信息  如果是遇到下面的msg 那就代表传回来的肯定是个byte[]
        // 根据我们要的方法进行解码 传回来的应该是方法的response的
        //根据需要的返回类型进行反序列化
        msg = serializationTool.deserialize(msgByteArray,method.getReturnType());

        response = msg;
        notify();
    }

    //调用的时候  就进行传输
    @Override
    public synchronized Object call() throws Exception {
        //这个变量的目的就是保留原来的param实际参数类型，当返回的时候 可以当作反序列化的模板
        Object request = param;
        //判断是否需要进行序列化 因为使用这个进行序列话 是我没有相应的解码器 2.4之后 就算是string也进行序列化
        request = serializationTool.serialize(request);

        //进一步进行压缩
        byte[] requestByteArray = (byte[]) request;

        //判断是否进行压缩
        if (openFunction)requestByteArray = compressTool.compress(requestByteArray);

        context.writeAndFlush(requestByteArray);

        wait();
        return response;
    }

    //用来判断是否出现了空闲事件  如果出现了那就进行相应的处理
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent)
        {
            IdleStateEvent event = (IdleStateEvent) evt;
            //设置一个事件字段
            String eventType = null;
            switch (event.state())
            {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()+"发生超时事件"+eventType+"：连接关闭");
            ctx.close();
        }
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
