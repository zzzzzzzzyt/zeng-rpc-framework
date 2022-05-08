package serialization;

import annotation.CodecSelector;
import com.fasterxml.jackson.core.JsonProcessingException;
import exception.RpcException;
import serialization.fst.FSTUtils;
import serialization.hessian.HessianUtils;
import serialization.json.FastJsonUtils;
import serialization.json.GsonUtils;
import serialization.json.JacksonUtils;
import serialization.kryo.KryoUtils;
import serialization.protostuff.ProtostuffUtils;

import java.io.IOException;

//这是一个进行统一序列化的一个工具
public class SerializationTool implements Serializer {

    static String codec = Serialization.class.getAnnotation(CodecSelector.class).Codec();

    @Override
    public byte[] serialize(Object obj) throws RpcException, JsonProcessingException {
        switch (codec) {
            case "kryo":
                return new KryoUtils().serialize(obj);
            case "protostuff":
                return new ProtostuffUtils().serialize(obj);
            case "hessian":
                return new HessianUtils().serialize(obj);
            case "fst":
                return new FSTUtils().serialize(obj);
            case "jackson":
                return new JacksonUtils().serialize(obj);
            case "fastjson":
                return new FastJsonUtils().serialize(obj);
            case "gson":
                return new GsonUtils().serialize(obj);
            default:
                throw new RpcException("你所找的序列化方法还没编写，或者可能在该版本被废弃了，你可以看看2.2版本");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws RpcException, IOException {
        switch (codec) {
            case "kryo":
                return new KryoUtils().deserialize(bytes, clazz);
            case "protostuff":
                return new ProtostuffUtils().deserialize(bytes, clazz);
            case "hessian":
                return new HessianUtils().deserialize(bytes, clazz);
            case "fst":
                return new FSTUtils().deserialize(bytes, clazz);
            case "jackson":
                return new JacksonUtils().deserialize(bytes,clazz);
            case "fastjson":
                return new FastJsonUtils().deserialize(bytes,clazz);
            case "gson":
                return new GsonUtils().deserialize(bytes,clazz);
            default:
                throw new RpcException("你所找的序列化方法还没编写，或者可能在该版本被废弃了，你可以看看2.2版本");
        }
    }
}
