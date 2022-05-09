package compress.lz4;


import compress.CompressTpye;
import net.jpountz.lz4.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class Lz4Utils implements CompressTpye {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public byte[] compress(byte[] bytes) throws IOException {
        //压缩的工具类吧
        LZ4Compressor compressor = LZ4Factory.fastestInstance().fastCompressor();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        LZ4BlockOutputStream lz4BlockOutputStream = new LZ4BlockOutputStream(outputStream,BUFFER_SIZE,compressor);
        lz4BlockOutputStream.write(bytes);

        lz4BlockOutputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public byte[] deCompress(byte[] bytes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        LZ4BlockInputStream decompressedInputStream = new LZ4BlockInputStream(inputStream,decompressor);
        int count;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((count=decompressedInputStream.read(buffer))!=-1)
        {
            outputStream.write(buffer,0,count);
        }
        decompressedInputStream.close();
        return outputStream.toByteArray();
    }
}
