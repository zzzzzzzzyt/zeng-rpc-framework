package annotation;

//两个参数分别代表的是什么方法和启用的数量

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解在类上  然后根据方法获得对应的属性进行判断
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcMethodCluster {
    String[] method();
    int[] startNum();
}
