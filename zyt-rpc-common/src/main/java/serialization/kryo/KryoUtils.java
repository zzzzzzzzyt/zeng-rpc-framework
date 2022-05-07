package serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import entity.Person;
import serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


//通过KryoUtils实现序列化
public class KryoUtils implements Serializer {


    /**
     * Because Kryo is not thread safe. So, use ThreadLocal to store Kryo objects
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(Person.class); //这里这个注册其实就有点类似于protostuff的scheme 就是一个模板
        // kryo.register(PersonPOJO.Person.class); //不能使用这个
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        //将其序列化  然后写入到输出流中
        //byte数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //需要的参数有Output是它自带的 要创建一个
        Output output = new Output(byteArrayOutputStream);
        Kryo kryo = kryoThreadLocal.get();
        kryo.writeObject(output,obj);
        kryoThreadLocal.remove();
        return output.toBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        //根据传入的bytes就知道里面是什么了  然后读取 转换
        Kryo kryo = kryoThreadLocal.get();
        //需要的参数有Input是它自带的
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        Object obj = kryo.readObject(input, clazz);
        kryoThreadLocal.remove();
        return (T) obj;
    }
}
