package serialization.hessian;



import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import lombok.extern.slf4j.Slf4j;
import serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class HessianUtils implements Serializer {

    //进行序列化
    @Override
    public byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HessianOutput hessianOutput = new HessianOutput(outputStream);
            hessianOutput.writeObject(obj);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return new byte[0];
    }

    //进行反序列化
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(inputStream);
        try {
            Object object = hessianInput.readObject(clazz);
            return clazz.cast(object);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
