package serialization;

import annotation.CodecSelector;

//直接选择默认方法 默认就是对jdk自带序列化进行编解码

/*
    ObjectCodec
    protoc
    kryo
    protostuff
    hessian
 */
@CodecSelector(Codec = "hessian")
public interface Serialization {
}
