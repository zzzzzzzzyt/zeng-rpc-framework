package consumer.proxy;


import exception.RpcException;

public class ClientProxyTool implements ClientProxy {
    @Override
    public Object getBean(Class<?> serviceClass) throws RpcException {
        return SPIClientProxyUtils.getUtils();
    }
}
