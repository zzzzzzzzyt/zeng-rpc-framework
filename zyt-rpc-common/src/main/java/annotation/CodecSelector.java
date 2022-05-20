package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//根据该注解判断选择当遇到传对象的相应的方法时，采用什么编解码方式

/**
 * @author 祝英台炸油条
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodecSelector {
    String Codec() default "ObjectCodec";
}
