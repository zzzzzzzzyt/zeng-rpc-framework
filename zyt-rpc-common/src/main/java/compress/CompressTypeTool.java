package compress;


//实际调用 通过SPI机制

import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class CompressTypeTool implements CompressType {
    //获取通过SPI机制获取的相应工具类
    static CompressType compressUtils;

    static {
        compressUtils = SPICompressUtils.getUtils();
        if (compressUtils==null)
        {
            try {
                throw new RpcException("相应工具类为空");
            } catch (RpcException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    @Override
    public byte[] compress(byte[] bytes) {
        return compressUtils.compress(bytes);

    }

    @Override
    public byte[] deCompress(byte[] bytes)  {
        return compressUtils.deCompress(bytes);
    }
}
