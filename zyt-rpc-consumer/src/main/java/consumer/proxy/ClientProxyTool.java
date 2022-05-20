package consumer.proxy;


/**
 * @author 祝英台炸油条
 */
public class ClientProxyTool implements ClientProxy {
    @Override
    public Object getBean(Class<?> serviceClass) {
        return SPIClientProxyUtils.getUtils().getBean(serviceClass);
    }
}
