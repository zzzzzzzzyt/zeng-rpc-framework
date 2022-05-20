package serialization.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import serialization.Serializer;

import java.io.IOException;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class JacksonUtils implements Serializer {

    //Jackson中的关键
    static ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        //将obj转换成json再将json转换成字节数组
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return new byte[0];
    }

    //将json字符串或者字节组转换为对应的类
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
