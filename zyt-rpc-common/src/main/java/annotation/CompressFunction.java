package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//进行判断解压缩功能是否开启
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CompressFunction {
    boolean isOpenFunction();
}
