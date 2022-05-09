package compress.gzip;

import compress.Compress;

import java.util.zip.GZIPOutputStream;

//gzip实现解压缩
public class GZipUtils implements Compress {
    @Override
    public byte[] compress(byte[] bytes) {
        return new byte[0];
    }

    @Override
    public byte[] deCompress(byte[] bytes) {
        return new byte[0];
    }
}
