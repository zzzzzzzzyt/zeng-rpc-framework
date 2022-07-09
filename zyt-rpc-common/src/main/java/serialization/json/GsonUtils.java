package serialization.json;

import com.google.gson.Gson;
import serialization.Serializer;

import java.nio.charset.StandardCharsets;

//gson

/**
 * @author 祝英台炸油条
 */
public class GsonUtils implements Serializer {
    static Gson gson = new Gson();

    @Override
    public byte[] serialize(Object obj) {
        return gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    //重点就是上下的编解码器  一定要保证字符串的正确性

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
}
