package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author ×£Ó¢Ì¨Õ¨ÓÍÌõ
 * @Time : 2022/5/20 20:55
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CompressSelector {
    String CompressTool();
}
