package compress.gzip;

import compress.CompressType;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

//gzip实现解压缩   实现过程和bzip很相似

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class GZipUtils implements CompressType {
    private static final int BUFFER_SIZE = 8192;

    @Override
    public byte[] compress(byte[] bytes) {
        //数组输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] request = new byte[0];
        try {
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bos);
            gzipOutputStream.write(bytes);
            gzipOutputStream.flush();
            //finish 当压缩结束后 结束
            gzipOutputStream.finish();
            request = bos.toByteArray();

            //关闭
            gzipOutputStream.close();
            bos.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return request;
    }

    @Override
    public byte[] deCompress(byte[] bytes) {

        //输入进行解压 再将解压后的传入输出
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        byte[] response = new byte[0];
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(bis);

            //将输入流中的数据写入
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gzipInputStream.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            response = out.toByteArray();

            //关闭对应
            out.close();
            gzipInputStream.close();
            bis.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return response;
    }
}
