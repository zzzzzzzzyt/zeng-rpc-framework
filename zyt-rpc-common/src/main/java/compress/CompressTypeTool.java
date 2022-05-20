package compress;


//实际调用 通过SPI机制

import annotation.CompressSelector;
import compress.bzip.BZipUtils;
import compress.deflater.DeflaterUtils;
import compress.diyzip.DiyZipUtils;
import compress.gzip.GZipUtils;
import compress.lz4.Lz4Utils;
import compress.zip.ZipUtils;
import configuration.GlobalConfiguration;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class CompressTypeTool implements CompressType {

    static CompressType compressUtils;

    // // 获取通过SPI机制获取的相应工具类  想用SPI机制开启这个即可
    // static {
    //     compressUtils = SPICompressUtils.getUtils();
    //     if (compressUtils == null) {
    //         try {
    //             throw new RpcException("相应工具类为空");
    //         } catch (RpcException e) {
    //             log.error(e.getMessage(), e);
    //         }
    //     }
    // }


    //通过注解获取对应的工具
    static {
        String compressTool = GlobalConfiguration.class.getAnnotation(CompressSelector.class).CompressTool();
        switch (compressTool) {
            case "BZipUtils":
                compressUtils = new BZipUtils();
                break;
            case "DeflaterUtils":
                compressUtils = new DeflaterUtils();
                break;
            case "GZipUtils":
                compressUtils = new GZipUtils();
                break;
            case "Lz4Utils":
                compressUtils = new Lz4Utils();
                break;
            case "ZipUtils":
                compressUtils = new ZipUtils();
                break;
            case "DiyZipUtils":
                compressUtils = new DiyZipUtils();
                break;
            default:
                try {
                    throw new RpcException("兄弟 尚未定义该器件");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                }
        }
    }


    @Override
    public byte[] compress(byte[] bytes) {
        return compressUtils.compress(bytes);

    }

    @Override
    public byte[] deCompress(byte[] bytes) {
        return compressUtils.deCompress(bytes);
    }
}
