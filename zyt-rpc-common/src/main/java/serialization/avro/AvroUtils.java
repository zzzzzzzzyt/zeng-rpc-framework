package serialization.avro;

import exception.RpcException;
import serialization.Serializer;

//
public class AvroUtils implements Serializer {
    @Override
    public byte[] serialize(Object obj) throws RpcException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws RpcException {
        return null;
    }
}
