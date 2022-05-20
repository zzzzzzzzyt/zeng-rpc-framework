package provider.netty_server_handler;


import annotation.RpcSerializationSelector;
import configuration.GlobalConfiguration;
import exception.RpcException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import serialization.hessian.HessianUtils;
import serialization.kryo.KryoUtils;
import serialization.protostuff.ProtostuffUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//实现简单的服务注册和回写

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NettyServerHandler22 extends ChannelInboundHandlerAdapter {
    private final String methodName;
    private boolean isProtostuff;
    private boolean isKryo;
    private boolean isHessian;

    //要传入对应的方法名 否则不知道 netty服务器能执行什么方法
    public NettyServerHandler22(String methodName) {
        this.methodName = methodName;
    }

    //实现对应的方法


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String serialization = GlobalConfiguration.class.getAnnotation(RpcSerializationSelector.class).RpcSerialization();
        switch (serialization) {
            case "protostuff":
                isProtostuff = true;
                break;
            case "kryo":
                isKryo = true;
                break;
            case "hessian":
                isHessian = true;
                break;
            default:
                try {
                    throw new RpcException("没有对应的序列化器");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("收到来自" + ctx.channel().remoteAddress() + "的信息");
        //使用反射的方法获取对应的类 通过反射再进行执行
        Class<?> calledClass = null;
        try {
            calledClass = Class.forName("provider.api." + methodName + "ServiceImpl");
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        assert calledClass != null;
        Method[] methods = calledClass.getMethods();
        Method method = methods[0];

        //如果我传入的是protostuff 传进来的是相应的byte数组 那我就找不到对应的方法 现在默认是第一个方法  还有就是判断不是字符串
        if (isProtostuff && msg.getClass() != String.class) {
            ProtostuffUtils protostuffUtils = new ProtostuffUtils();
            msg = protostuffUtils.deserialize((byte[]) msg, method.getParameterTypes()[0]);
        } else if (isKryo && msg.getClass() != String.class) {
            KryoUtils kryoUtils = new KryoUtils();
            msg = kryoUtils.deserialize((byte[]) msg, method.getParameterTypes()[0]);
        } else if (isHessian && msg.getClass() != String.class) {
            HessianUtils hessianUtils = new HessianUtils();
            msg = hessianUtils.deserialize((byte[]) msg, method.getParameterTypes()[0]);
        } else
        //因为我进行重写了 内部会有多个实现方法  所以就按照对应的传入参数 来判断是哪个方法
        {
            for (int i = 0; i < methods.length; ++i) {
                if (methods[i].getParameterTypes()[0] == msg.getClass()) {
                    method = methods[i];
                    break;
                }
            }
        }

        Object response = null;
        try {
            Object instance = calledClass.newInstance();
            response = method.invoke(instance, msg);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        //获得对应信息并进行回传

        //判断是否需要通过对应的方法进行序列化
        assert response != null;
        if (isProtostuff && response.getClass() != String.class) {
            ProtostuffUtils protostuffUtils = new ProtostuffUtils();
            response = protostuffUtils.serialize(response);
        }
        if (isKryo && response.getClass() != String.class) {
            KryoUtils kryoUtils = new KryoUtils();
            response = kryoUtils.serialize(response);
        }
        if (isHessian && response.getClass() != String.class) {
            HessianUtils hessianUtils = new HessianUtils();
            response = hessianUtils.serialize(response);
        }

        ctx.writeAndFlush(response);
    }

    //出现异常的话 如何进行处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
        log.error(cause.getMessage(), cause);
    }
}
