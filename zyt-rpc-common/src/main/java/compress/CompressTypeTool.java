package compress;


import exception.RpcException;

import java.io.IOException;
import java.util.zip.DataFormatException;

//实际调用
public class CompressTypeTool implements CompressType {
    //获取通过SPI机制获取的相应工具类
    static CompressType compressUtils;

    static {
        try {
            compressUtils = SPICompressUtils.getUtils();
        } catch (RpcException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] compress(byte[] bytes) throws IOException {
        return compressUtils.compress(bytes);

    }

    @Override
    public byte[] deCompress(byte[] bytes) throws IOException, DataFormatException {
        return compressUtils.deCompress(bytes);
    }
}
