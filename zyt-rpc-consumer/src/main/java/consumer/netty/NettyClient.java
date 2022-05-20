package consumer.netty;

import java.lang.reflect.Method;

//如果对应的是字符串类那就不用到下面的这个里面调用了
/**
 * @author 祝英台炸油条
 */
public class NettyClient {
    public static Object callMethod(String hostName, int port, Object param, Method method){
        //用户根据自己想用的版本 打开对应的注解
        // return NettyClient21.callMethod(hostName, port, param,method);
        // return NettyClient22.callMethod(hostName, port, param,method);
        return NettyClient24.callMethod(hostName, port, param,method);
    }
}
