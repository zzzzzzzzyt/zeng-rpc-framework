package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//是否开启的工具类

/**
 * @author 祝英台炸油条
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeartBeatTool {
    boolean isOpenFunction() default false; //是否开启的标志

    int readerIdleTimeSeconds() default 5;  //超过多少时间触发读空闲事件

    int writerIdleTimeSeconds() default 5;  //超过多少时间触发写空闲事件

    int allIdleTimeSeconds() default 3;     //超过多少时间触发读写空闲事件
}
