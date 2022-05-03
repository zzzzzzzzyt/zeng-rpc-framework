package constants;


import java.util.Properties;

public class RpcConstants {
    static {

    }
    //zookeeper服务器连接地址
    public static String ZOOKEEPER_ADDRESS = "zytCentos:2181";
    //超时时间
    public static int ZOOKEEPER_SESSION_TIMEOUT = 2000;


    public static String NACOS_DISCOVERY_ADDRESS = "http://192.168.18.128:8848/nacos/v1/ns/instance/list?";

    //找到对应要注册的地方
    public static Properties NACOS_PROPERTIES= new Properties() ;

    //serverAddr nacos的地址
    //namespace 存放的服务列表
    public static Properties propertiesInit()
    {
        NACOS_PROPERTIES.setProperty("serverAddr","192.168.18.128:8848");
        NACOS_PROPERTIES.setProperty("namespace","public");
        return NACOS_PROPERTIES;
    }
}
