package codec;

import annotation.CodecSelector;
import annotation.RpcSerializationSelector;
import configuration.GlobalConfiguration;
import entity.PersonPOJO;
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
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

//公共类 根据对应选择的注解进行编码器的添加

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class AddCodec {


    public static void addCodec(ChannelPipeline pipeline, Method method, boolean isConsumer) {
        //根据注解进行编解码器的选择
        RpcSerializationSelector annotation = GlobalConfiguration.class.getAnnotation(RpcSerializationSelector.class);

        //目前而来我的传输 传入的参数都是一个 所以根据这一个传入和返回的参数的类型进行判断
        //下面是我传入的参数 和传出的参数
        Class<?> returnType = method.getReturnType();
        Class<?> parameterType = method.getParameterTypes()[0];

        String rpcSerialization = annotation.RpcSerialization();
        switch (rpcSerialization) {
            case "ObjectCodec": //2.2版本之前会使用
                if (returnType != String.class && parameterType != String.class) {
                    pipeline.addLast(new ObjectEncoder());
                    //传的参是固定写法
                    pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                            ClassResolvers.weakCachingResolver(null)));
                } else if (returnType != String.class && parameterType == String.class) {
                    //如果是客户端的话那么传出的是服务端传入的  所以这边编码那边就是解码
                    if (isConsumer) {
                        //根据传入传出进行对应的编码
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.weakCachingResolver(null)));
                    } else {
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new StringDecoder());
                    }

                } else if (parameterType != String.class) {
                    //客户端 会对参数进行编码，服务端是解码
                    if (isConsumer) {
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new StringDecoder());
                    } else {
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.weakCachingResolver(null)));
                    }
                } else {
                    //因为传入参数和传出都是字符串类型 所以就传入字符串编解码器
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                return;
            case "protoc":  //添加protobuf的编解码器   如果是protobuf的编解码器的话 那可能还需要一点其他操作  2.2版本之前会使用
                if (returnType != String.class && parameterType != String.class) {
                    pipeline.addLast(new ProtobufEncoder());
                    //对什么实例解码
                    pipeline.addLast(new ProtobufDecoder(PersonPOJO.Person.getDefaultInstance()));
                } else if (returnType != String.class) {
                    //如果是客户端的话那么传出的是服务端传入的  所以这边编码那边就是解码
                    if (isConsumer) {
                        //根据传入传出进行对应的编码
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new ProtobufDecoder(PersonPOJO.Person.getDefaultInstance()));
                        ;
                    } else {
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new StringDecoder());
                    }
                } else if (parameterType != String.class) {
                    //客户端 会对参数进行编码，服务端是解码
                    if (isConsumer) {
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new StringDecoder());
                    } else {
                        pipeline.addLast(new StringEncoder());
                        //这个就是获取对应的实例 必须要这样传
                        pipeline.addLast(new ProtobufDecoder(PersonPOJO.Person.getDefaultInstance()));
                    }
                } else {
                    //因为传入参数和传出都是字符串类型 所以就传入字符串编解码器
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                }
                return;
            //下面的都是通过转换成字节数组 然后再转换回来 所以不需要和上面一样
            case "hessian":
            case "kryo":
            case "protostuff":
            case "fst":
            case "jackson":
            case "fastjson":
            case "gson":
                /*
                    2.4版本之前打开注解
                 */
                // if (returnType!=String.class&&parameterType!=String.class)
                // {
                //     //都进行序列化后传输
                //     pipeline.addLast(new ByteArrayEncoder());
                //     pipeline.addLast(new ByteArrayDecoder());
                // }
                // else if (returnType!=String.class&&parameterType==String.class)
                // {
                //     //如果是客户端的话那么传出的是服务端传入的  所以这边编码那边就是解码
                //     if (isConsumer)
                //     {
                //         //根据传入传出进行对应的编码
                //         pipeline.addLast(new StringEncoder());
                //         pipeline.addLast(new ByteArrayDecoder());;
                //     }
                //     else
                //     {
                //         pipeline.addLast(new StringDecoder());
                //         pipeline.addLast(new ByteArrayEncoder());
                //     }
                // }
                // else if (returnType==String.class&&parameterType!=String.class)
                // {
                //     //客户端 会对参数进行编码，服务端是解码
                //     if (isConsumer)
                //     {
                //         pipeline.addLast(new ByteArrayEncoder());
                //         pipeline.addLast(new StringDecoder());
                //     }
                //     else
                //     {
                //         pipeline.addLast(new StringEncoder());
                //         //这个就是获取对应的实例 必须要这样传
                //         pipeline.addLast(new ByteArrayDecoder());
                //     }
                // }
                // else
                // {
                //     //因为传入参数和传出都是字符串类型 所以就传入字符串编解码器
                //     pipeline.addLast(new StringEncoder());
                //     pipeline.addLast(new StringDecoder());
                // }
                /*
                    2.4版本之前 注释底下
                 */
                pipeline.addLast(new ByteArrayEncoder());
                pipeline.addLast(new ByteArrayDecoder());
                return;
            default:  //如果都不是那就不加了
                try {
                    throw new RpcException("兄弟 你并没有指定相应的方法 我无法进行编解码器");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                }
        }
    }
}
