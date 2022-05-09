package compress;

//实现压缩的接口
public interface Compress {
    //压缩方法
    public byte[] compress(byte[] bytes);
    //解压方法
    public byte[] deCompress(byte[] bytes);
}
