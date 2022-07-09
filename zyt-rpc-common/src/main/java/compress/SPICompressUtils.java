package compress;


import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

//通过SPI机制获取对应需要的解压缩工具类  而不是用注解类获取
@Slf4j

public class SPICompressUtils {
    public static CompressType getUtils() {
        ServiceLoader<CompressType> loader = ServiceLoader.load(CompressType.class);
        CompressType resultType;
        for (CompressType compressType : loader) {
            resultType = compressType;
            return resultType;
        }
        try {
            throw new RpcException("没有去配置对应的解压缩方法");
        } catch (RpcException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
