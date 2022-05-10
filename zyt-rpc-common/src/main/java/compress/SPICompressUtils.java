package compress;


import exception.RpcException;

import java.util.ServiceLoader;

//通过SPI机制获取对应需要的解压缩工具类  而不是用注解类获取
public class SPICompressUtils {
    public static CompressType getUtils() throws RpcException {
        ServiceLoader<CompressType> loader = ServiceLoader.load(CompressType.class);
        for (CompressType compressType : loader) {
            return compressType;
        }
        throw new RpcException("没有去配置对应的解压缩方法");
    }
}
