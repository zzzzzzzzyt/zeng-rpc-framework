package serialization.fst;


import org.nustaq.serialization.FSTConfiguration;
import serialization.Serializer;

//一款较新的序列化工具
/**
 * @author 祝英台炸油条
 */
public class FSTUtils implements Serializer {

    //需要弄懂 rather than
    static FSTConfiguration fstConfiguration = FSTConfiguration.createStructConfiguration();

    //进行序列化
    @Override
    public byte[] serialize(Object obj) {
        return fstConfiguration.asByteArray(obj);
    }

    //进行反序列化
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz)  {
        return clazz.cast(fstConfiguration.asObject(bytes));
    }
}
