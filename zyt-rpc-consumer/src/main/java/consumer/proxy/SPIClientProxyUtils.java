package consumer.proxy;

import exception.RpcException;

import java.util.ServiceLoader;

public class SPIClientProxyUtils {
    public static ClientProxy getUtils() throws RpcException {
        ServiceLoader<ClientProxy> loader = ServiceLoader.load(ClientProxy.class);
        for (ClientProxy clientProxy : loader) {
            return clientProxy;
        }
        throw new RpcException("您键入的代理，并未实现，欢迎实现提出pr");
    }
}
