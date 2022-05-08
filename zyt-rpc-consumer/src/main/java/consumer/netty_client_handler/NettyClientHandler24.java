package consumer.netty_client_handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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

        //在这要进行解码 获得传回来的信息  如果是遇到下面的msg 那就代表传回来的肯定是个byte[]
        // 根据我们要的方法进行解码 传回来的应该是方法的response的
        //根据需要的返回类型进行反序列化
        msg = serializationTool.deserialize((byte[]) msg,method.getReturnType());

        response = msg;
        notify();
    }

    //调用的时候  就进行传输
    @Override
    public synchronized Object call() throws Exception {

        //这个变量的目的就是保留原来的param实际参数类型，当返回的时候 可以当作反序列化的模板
        Object request = param;
        //判断是否需要protostuff进行序列化 因为使用这个进行序列话 是我没有相应的解码器 2.4之后 就算是string也进行序列化

        request = serializationTool.serialize(request);

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
