package codec;

import annotation.CodecSelector;
import exception.RpcException;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import serialization.Serialization;
import entity.PersonPOJO;

import java.lang.reflect.Method;

//公共类 根据对应选择的注解进行编码器的添加
public class AddCodec {

    /**
     *
     * @param pipeline
     * @param method 方法，来获取对应的输入输出类
     * @throws RpcException
     */
    public static  void addCodec(ChannelPipeline pipeline, Method method,boolean isConsumer) throws RpcException, InstantiationException, IllegalAccessException {
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
            case "protoc":  //添加protobuf的编解码器   如果是protobuf的编解码器的话 那可能还需要一点其他操作
                if (returnType!=String.class&&parameterType!=String.class)
                {
                    pipeline.addLast(new ProtobufEncoder());
                    //对什么实例解码
                    pipeline.addLast(new ProtobufDecoder(PersonPOJO.Person.getDefaultInstance()));
                }
                else if (returnType!=String.class&&parameterType==String.class)
                {
                    //如果是客户端的话那么传出的是服务端传入的  所以这边编码那边就是解码
                    if (isConsumer)
                    {
                        //根据传入传出进行对应的编码
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ProtobufDecoder(PersonPOJO.Person.getDefaultInstance()));;
                    }
                    else
                    {
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new StringDecoder());
                    }
                }
                else if (returnType==String.class&&parameterType!=String.class)
                {
                    //客户端 会对参数进行编码，服务端是解码
                    if (isConsumer)
                    {
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new StringDecoder());
                    }
                    else
                    {
                        pipeline.addLast(new StringEncoder());
                        //这个就是获取对应的实例 必须要这样传
                        pipeline.addLast(new ProtobufDecoder(PersonPOJO.Person.getDefaultInstance()));;
                    }
                }
                else
                {
                    //因为传入参数和传出都是字符串类型 所以就传入字符串编解码器
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                return;
            case "kryo": //添加kryo的编解码器   kryo和protostuff的编解码器其实一样 因为都是byteArray或者string在传输
                if (returnType!=String.class&&parameterType!=String.class)
                {
                    //都进行序列化后传输
                    pipeline.addLast(new ByteArrayEncoder());
                    pipeline.addLast(new ByteArrayDecoder());
                }
                else if (returnType!=String.class&&parameterType==String.class)
                {
                    //如果是客户端的话那么传出的是服务端传入的  所以这边编码那边就是解码
                    if (isConsumer)
                    {
                        //根据传入传出进行对应的编码
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ByteArrayDecoder());;
                    }
                    else
                    {
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new ByteArrayEncoder());
                    }
                }
                else if (returnType==String.class&&parameterType!=String.class)
                {
                    //客户端 会对参数进行编码，服务端是解码
                    if (isConsumer)
                    {
                        pipeline.addLast(new ByteArrayEncoder());
                        pipeline.addLast(new StringDecoder());
                    }
                    else
                    {
                        pipeline.addLast(new StringEncoder());
                        //这个就是获取对应的实例 必须要这样传
                        pipeline.addLast(new ByteArrayDecoder());
                    }
                }
                else
                {
                    //因为传入参数和传出都是字符串类型 所以就传入字符串编解码器
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                return;
            case "protostuff": //在处理器那边进行判断 如果是解析器 是这个的话 就在上下加上编解码过程
                if (returnType!=String.class&&parameterType!=String.class)
                {
                    //都进行序列化后传输
                    pipeline.addLast(new ByteArrayEncoder());
                    pipeline.addLast(new ByteArrayDecoder());
                }
                else if (returnType!=String.class&&parameterType==String.class)
                {
                    //如果是客户端的话那么传出的是服务端传入的  所以这边编码那边就是解码
                    if (isConsumer)
                    {
                        //根据传入传出进行对应的编码
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ByteArrayDecoder());;
                    }
                    else
                    {
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new ByteArrayEncoder());
                    }
                }
                else if (returnType==String.class&&parameterType!=String.class)
                {
                    //客户端 会对参数进行编码，服务端是解码
                    if (isConsumer)
                    {
                        pipeline.addLast(new ByteArrayEncoder());
                        pipeline.addLast(new StringDecoder());
                    }
                    else
                    {
                        pipeline.addLast(new StringEncoder());
                        //这个就是获取对应的实例 必须要这样传
                        pipeline.addLast(new ByteArrayDecoder());
                    }
                }
                else
                {
                    //因为传入参数和传出都是字符串类型 所以就传入字符串编解码器
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                return;
            default:  //如果都不是那就不加了
                return;
        }
    }
}
