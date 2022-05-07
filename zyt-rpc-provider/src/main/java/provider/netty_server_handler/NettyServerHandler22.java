package provider.netty_server_handler;


import annotation.CodecSelector;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import serialization.Serialization;
import serialization.kryo.KryoUtils;
import serialization.protostuff.ProtostuffUtils;

import java.lang.reflect.Method;

//实现简单的服务注册和回写
public class NettyServerHandler22 extends ChannelInboundHandlerAdapter {
    private String methodName;
    private boolean isProtostuff;
    private boolean isKryo;
    //要传入对应的方法名 否则不知道 netty服务器能执行什么方法
    public NettyServerHandler22(String methodName)
    {
        this.methodName = methodName;
    }

    //实现对应的方法


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String codecType = Serialization.class.getAnnotation(CodecSelector.class).Codec();
        if (codecType.equals("protostuff"))isProtostuff = true;
        else if (codecType.equals("kryo"))isKryo = true;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到来自"+ctx.channel().remoteAddress()+"的信息");
        //使用反射的方法获取对应的类 通过反射再进行执行
        Class<?> calledClass = Class.forName("provider.api."+methodName + "ServiceImpl");
        Method[] methods = calledClass.getMethods();
        Method method = methods[0];

        //如果我传入的是protostuff 传进来的是相应的byte数组 那我就找不到对应的方法 现在默认是第一个方法  还有就是判断不是字符串
        if (isProtostuff&&msg.getClass()!=String.class)
        {
            ProtostuffUtils protostuffUtils = new ProtostuffUtils();
            msg = protostuffUtils.deserialize((byte[]) msg, method.getParameterTypes()[0]);
        }
        else if (isKryo&&msg.getClass()!=String.class)
        {
            KryoUtils kryoUtils = new KryoUtils();
            msg = kryoUtils.deserialize((byte[]) msg, method.getParameterTypes()[0]);
        }
        else
        //因为我进行重写了 内部会有多个实现方法  所以就按照对应的传入参数 来判断是哪个方法
        {
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getParameterTypes()[0]==msg.getClass())
                {
                    method = methods[i];
                    break;
                }
            }
        }

        Object instance = calledClass.newInstance();
        Object response = method.invoke(instance, msg);
        //获得对应信息并进行回传

        //判断是否需要通过对应的方法进行序列化
        if (isProtostuff&&response.getClass()!=String.class)
        {
            ProtostuffUtils protostuffUtils = new ProtostuffUtils();
            response = protostuffUtils.serialize(response);
        }
        if (isKryo&&response.getClass()!=String.class)
        {
            KryoUtils kryoUtils = new KryoUtils();
            response = kryoUtils.serialize(response);
        }

        ctx.writeAndFlush(response);
    }

    //出现异常的话 如何进行处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        cause.printStackTrace();
    }
}
