package compress.lz4;

import compress.CompressType;
import lombok.extern.slf4j.Slf4j;
import net.jpountz.lz4.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class Lz4Utils implements CompressType {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public byte[] compress(byte[] bytes) {
        //压缩的工具类吧
        LZ4Compressor compressor = LZ4Factory.fastestInstance().fastCompressor();
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            LZ4BlockOutputStream lz4BlockOutputStream = new LZ4BlockOutputStream(outputStream, BUFFER_SIZE, compressor);
            lz4BlockOutputStream.write(bytes);
            lz4BlockOutputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return outputStream.toByteArray();
    }

    @Override
    public byte[] deCompress(byte[] bytes) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        LZ4FastDecompressor decompressor = LZ4Factory.fastestInstance().fastDecompressor();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        try {
            LZ4BlockInputStream decompressedInputStream = new LZ4BlockInputStream(inputStream, decompressor);
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((count = decompressedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
            decompressedInputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return outputStream.toByteArray();
    }
}
