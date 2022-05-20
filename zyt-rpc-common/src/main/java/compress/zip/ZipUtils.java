package compress.zip;

import compress.CompressType;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtils implements CompressType {
    private static final int BUFFER_SIZE = 8192;
    @Override
    public byte[] compress(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ZipOutputStream zip = new ZipOutputStream(out);
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(bytes.length);
            zip.putNextEntry(entry);
            zip.write(bytes);
            zip.closeEntry();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return out.toByteArray();
    }

    @Override
    public byte[] deCompress(byte[] bytes){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(bytes));
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            while (zip.getNextEntry()!=null)
            {
                int n;
                while ((n = zip.read(buffer))!=-1)
                {
                    out.write(buffer,0,n);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return out.toByteArray();
    }
}
