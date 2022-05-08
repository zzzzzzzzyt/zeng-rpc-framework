package serialization.hessian;



import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import exception.RpcException;
import serialization.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class HessianUtils implements Serializer {

    //进行序列化
    @Override
    public byte[] serialize(Object obj) throws RpcException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HessianOutput hessianOutput = new HessianOutput(outputStream);
            hessianOutput.writeObject(obj);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RpcException("序列化失败");
        }
    }

    //进行反序列化
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws RpcException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(inputStream);
        try {
            Object object = hessianInput.readObject(clazz);
            return clazz.cast(object);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RpcException("反序列化失败");
        }
    }
}
