package serialization.fst;

import exception.RpcException;
import serialization.Serializer;
import org.nustaq.serialization.FSTConfiguration;

//一款较新的序列化工具
public class FSTUtils implements Serializer {

    //需要弄懂 rather than
    static FSTConfiguration fstConfiguration = FSTConfiguration.createStructConfiguration();

    //进行序列化
    @Override
    public byte[] serialize(Object obj) throws RpcException {
        return fstConfiguration.asByteArray(obj);
    }

    //进行反序列化
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws RpcException {
        return clazz.cast(fstConfiguration.asObject(bytes));
    }
}
