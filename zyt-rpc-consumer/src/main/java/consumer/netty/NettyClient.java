package consumer.netty;

import java.lang.reflect.Method;

//如果对应的是字符串类那就不用到下面的这个里面调用了
public class NettyClient {
    public static Object callMethod(String hostName, int port, Object param, Method method) throws Exception {
        return NettyClient22.callMethod(hostName, port, param,method);
    }
}
