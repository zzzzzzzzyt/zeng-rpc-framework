package consumer.proxy;


import java.util.Objects;

/**
 * @author 祝英台炸油条
 */
public class ClientProxyTool implements ClientProxy {
    @Override
    public Object getBean(Class<?> serviceClass) {
        return Objects.requireNonNull(SPIClientProxyUtils.getUtils()).getBean(serviceClass);
    }
}
