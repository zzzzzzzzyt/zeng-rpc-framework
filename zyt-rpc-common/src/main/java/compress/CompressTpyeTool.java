package compress;

import compress.bzip.BZipUtils;
import compress.deflater.DeflaterUtils;
import compress.gzip.GZipUtils;
import compress.lz4.Lz4Utils;
import compress.zip.ZipUtils;


import java.io.IOException;
import java.util.zip.DataFormatException;

//实际调用
public class CompressTpyeTool implements CompressTpye {
    ZipUtils bZipUtils = new ZipUtils();
    @Override
    public byte[] compress(byte[] bytes) throws IOException {
        return bZipUtils.compress(bytes);

    }

    @Override
    public byte[] deCompress(byte[] bytes) throws IOException, DataFormatException {
        return bZipUtils.deCompress(bytes);
    }
}
