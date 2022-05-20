package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解 通过此注解可以判断当前是哪一个版本  选择调用哪个版本的服务端启动器
/**
 * @author 祝英台炸油条
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcServerBootStrap {
    String version();
}
