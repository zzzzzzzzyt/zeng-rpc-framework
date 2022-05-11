package consumer.proxy;

import exception.RpcException;

//模板
public interface ClientProxy {
    public  Object getBean(final Class<?> serviceClass) throws RpcException;
}
