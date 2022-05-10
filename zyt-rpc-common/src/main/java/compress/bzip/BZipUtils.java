package compress.bzip;

import compress.CompressType;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

//apache下的一个解压缩工具 bzip
public class BZipUtils implements CompressType {

    private static final int BUFFER_SIZE = 8192;
    @Override
    public byte[] compress(byte[] bytes) throws IOException {
        //数组输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CBZip2OutputStream cbZip2OutputStream = new CBZip2OutputStream(bos);
        cbZip2OutputStream.write(bytes);
        //finish 当压缩结束后 结束
        cbZip2OutputStream.finish();
        byte[] request = bos.toByteArray();

        //关闭
        cbZip2OutputStream.close();
        bos.close();

        return request;
    }

    @Override
    public byte[] deCompress(byte[] bytes) throws IOException {
        //输入进行解压 再将解压后的传入输出
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        CBZip2InputStream cbZip2InputStream = new CBZip2InputStream(bis);

        //将输入流中的数据写入
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int n;
        while ((n=cbZip2InputStream.read(buffer))>-1)
        {
            out.write(buffer,0,n);
        }
        byte[] response = out.toByteArray();

        //关闭对应
        out.close();
        cbZip2InputStream.close();
        bis.close();

        return response;
    }
}
