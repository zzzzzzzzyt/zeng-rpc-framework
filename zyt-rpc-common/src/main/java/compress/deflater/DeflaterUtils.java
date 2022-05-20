package compress.deflater;

import compress.CompressType;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class DeflaterUtils implements CompressType {

    //固定读取字节数组大小
    private static final int BUFFER_SIZE = 8192;

    @Override
    public byte[] compress(byte[] bytes) {
        int length;
        Deflater deflater = new Deflater();
        deflater.setInput(bytes);
        deflater.finish();

        byte[] outputBytes = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (!deflater.finished()) {
            length = deflater.deflate(outputBytes);
            bos.write(outputBytes, 0, length);
        }
        deflater.end();
        return bos.toByteArray();
    }

    //解压
    @Override
    public byte[] deCompress(byte[] bytes) {
        int length;
        ByteArrayOutputStream bos = null;
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(bytes);
            byte[] outputBytes = new byte[BUFFER_SIZE];
            bos = new ByteArrayOutputStream();
            while (!inflater.finished()) {
                length = inflater.inflate(outputBytes);
                bos.write(outputBytes, 0, length);
            }
            inflater.end();
        } catch (DataFormatException e) {
            log.error(e.getMessage(), e);
        }
        return bos.toByteArray();
    }
}
