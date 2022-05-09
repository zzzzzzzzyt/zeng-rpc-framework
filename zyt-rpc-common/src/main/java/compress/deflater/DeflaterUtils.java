package compress.deflater;

import compress.CompressTpye;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DeflaterUtils implements CompressTpye {

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
        while (!deflater.finished())
        {
            length = deflater.deflate(outputBytes);
            bos.write(outputBytes,0,length);
        }
        deflater.end();
        return bos.toByteArray();
    }

    //解压
    @Override
    public byte[] deCompress(byte[] bytes) throws DataFormatException {
        int length;
        Inflater inflater = new Inflater();
        inflater.setInput(bytes);
        byte[] outputBytes = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (!inflater.finished())
        {
            length = inflater.inflate(outputBytes);
            bos.write(outputBytes,0,length);
        }
        inflater.end();
        return bos.toByteArray();
    }
}
