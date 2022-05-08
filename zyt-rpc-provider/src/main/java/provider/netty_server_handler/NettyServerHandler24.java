package provider.netty_server_handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import serialization.SerializationTool;
import java.lang.reflect.Method;


/*
 *   在2.4版本之后我们就暂时淘汰JDK序列化和protoc编译成的类进行处理了
 * */
//实现简单的服务注册和回写
public class NettyServerHandler24 extends ChannelInboundHandlerAdapter {
    private String methodName;
    //要传入对应的方法名 否则不知道 netty服务器能执行什么方法
    public NettyServerHandler24(String methodName)
    {
        this.methodName = methodName;
    }

    //静态实现序列化工具类
    static SerializationTool serializationTool = new SerializationTool();
    //实现对应的方法

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到来自"+ctx.channel().remoteAddress()+"的信息");
        //使用反射的方法获取对应的类 通过反射再进行执行
        Class<?> calledClass = Class.forName("provider.api."+methodName + "ServiceImpl");
        Method[] methods = calledClass.getMethods();
        Method method = methods[0];

        //如果传入的不是数组 就去进行反序列化

        msg = serializationTool.deserialize((byte[]) msg,method.getParameterTypes()[0]);

        //因为我进行重写了 内部会有多个实现方法  所以就按照对应的传入参数 来判断是哪个方法  因为没有了protoc编译了 所以肯定是第一个
        // for (int i = 0; i < methods.length; i++) {
        //     if (methods[i].getParameterTypes()[0]==msg.getClass())
        //     {
        //         method = methods[i];
        //         break;
        //     }
        // }


        Object instance = calledClass.newInstance();
        Object response = method.invoke(instance, msg);
        //获得对应信息并进行回传

        //判断是否需要通过对应的方法进行序列化  序列化都集成了
        response = serializationTool.serialize(response);

        ctx.writeAndFlush(response);
    }

    //出现异常的话 如何进行处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }
}
