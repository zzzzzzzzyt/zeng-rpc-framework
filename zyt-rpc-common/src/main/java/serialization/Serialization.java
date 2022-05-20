package serialization;

import annotation.CodecSelector;

//直接选择默认方法 默认就是对jdk自带序列化进行编解码  已经过时了 配置到统一配置类中了

/*
    ObjectCodec
    protoc
    kryo
    protostuff
    hessian
    fst
    avro
    jackson
    fastjson
    gson
 */
@Deprecated
@CodecSelector(Codec = "fastjson")
public interface Serialization {
}
