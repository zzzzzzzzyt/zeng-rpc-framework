package serialization.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.RpcException;
import serialization.Serializer;

import java.io.IOException;

public class JacksonUtils implements Serializer {

    //Jackson中的关键
    static ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) throws RpcException, JsonProcessingException {
        //将obj转换成json再将json转换成字节数组
        return mapper.writeValueAsBytes(obj);
    }

    //将json字符串或者字节组转换为对应的类
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws RpcException, IOException {
        return mapper.readValue(bytes,clazz);
    }
}
