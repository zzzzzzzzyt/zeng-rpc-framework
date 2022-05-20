package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Rpc序列化方法的选择  因为序列化选择  我们采用了SPI方式 这个就暂时搁置
/**
 * @author 祝英台炸油条
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcSerializationSelector {
    String RpcSerialization();
}
