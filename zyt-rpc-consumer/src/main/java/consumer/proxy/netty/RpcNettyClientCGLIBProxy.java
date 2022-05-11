package consumer.proxy.netty;

import consumer.proxy.ClientProxy;

//Cglib实现代理模式
public class RpcNettyClientCGLIBProxy implements ClientProxy {
    @Override
    public Object getBean(Class<?> serviceClass) {
        return null;
    }
}
