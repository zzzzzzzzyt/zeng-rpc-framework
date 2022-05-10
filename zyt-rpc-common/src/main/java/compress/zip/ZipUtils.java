package compress.zip;

import compress.CompressType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils implements CompressType {
    private static final int BUFFER_SIZE = 8192;
    @Override
    public byte[] compress(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(out);
        ZipEntry entry = new ZipEntry("zip");
        entry.setSize(bytes.length);
        zip.putNextEntry(entry);
        zip.write(bytes);
        zip.closeEntry();
        return out.toByteArray();
    }

    @Override
    public byte[] deCompress(byte[] bytes) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(bytes));
        byte[] buffer = new byte[BUFFER_SIZE];
        while (zip.getNextEntry()!=null)
        {
            int n;
            while ((n = zip.read(buffer))!=-1)
            {
                out.write(buffer,0,n);
            }
        }
        return out.toByteArray();
    }
}
