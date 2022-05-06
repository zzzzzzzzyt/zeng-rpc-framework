package codec;

import annotation.CodecSelector;
import exception.RpcException;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import serialization.Serialization;

import java.lang.reflect.Method;

//公共类 根据对应选择的注解进行编码器的添加
public class AddCodec {

    /**
     *
     * @param pipeline
     * @param method 方法，来获取对应的输入输出类
     * @throws RpcException
     */
    public static  void addCodec(ChannelPipeline pipeline, Method method,boolean isConsumer) throws RpcException {
        //根据注解进行编解码器的选择
        CodecSelector annotation = Serialization.class.getAnnotation(CodecSelector.class);

        //目前而来我的传输 传入的参数都是一个 所以根据这一个传入和返回的参数的类型进行判断
        //下面是我传入的参数 和传出的参数
        Class<?> returnType = method.getReturnType();
        Class<?> parameterType = method.getParameterTypes()[0];

        String codec = annotation.Codec();
        switch (codec)
        {
            case "ObjectCodec":
                if (returnType!=String.class&&parameterType!=String.class)
                {
                    pipeline.addLast(new ObjectEncoder());
                    //传的参是固定写法
                    pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.weakCachingResolver(null)));
                }
                else if (returnType!=String.class&&parameterType==String.class)
                {
                    //如果是客户端的话那么传出的是服务端传入的  所以这边编码那边就是解码
                    if (isConsumer)
                    {
                        //根据传入传出进行对应的编码
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.weakCachingResolver(null)));
                    }
                    else
                    {
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new StringDecoder());
                    }

                }
                else if (returnType==String.class&&parameterType!=String.class)
                {
                    //客户端 会对参数进行编码，服务端是解码
                    if (isConsumer)
                    {
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new StringDecoder());
                    }
                    else
                    {
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.weakCachingResolver(null)));
                    }
                }
                else
                {
                    //因为传入参数和传出都是字符串类型 所以就传入字符串编解码器
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                return;
            case "protobuf":  //添加protobuf的编解码器
                return;
            case "kryo": //添加kryo的编解码器
                return;
            default:  //如果都不是那就不加了
                return;
        }
    }
}
