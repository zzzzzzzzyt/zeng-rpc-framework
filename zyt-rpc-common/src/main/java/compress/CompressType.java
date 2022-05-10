package compress;

import java.io.IOException;
import java.util.zip.DataFormatException;

//实现压缩的接口
public interface CompressType {
    //压缩方法
    public byte[] compress(byte[] bytes) throws IOException;
    //解压方法
    public byte[] deCompress(byte[] bytes) throws IOException, DataFormatException;
}
