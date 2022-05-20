package consumer.proxy;

import exception.RpcException;

//模板
/**
 * @author 祝英台炸油条
 */
public interface ClientProxy {
    Object getBean(final Class<?> serviceClass) throws RpcException;
}
