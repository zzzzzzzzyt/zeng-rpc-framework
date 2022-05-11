import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


//测试cglib是否可以实现接口的调用 确实是可以的
public class CGLIBTest {
    public static void main(String[] args) {
        // System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\gitCode\\mySpring\\DcumentReader\\src\\main\\java\\com\\tqy\\document\\reader\\extention\\demo1");

        TestCGLib testCGLib = new TestCGLib();
        TestInterface o = (TestInterface) testCGLib.getInstance(TestInterface.class);
        System.out.println(o.getHalloWorld());
    }
}
class TestCGLib implements MethodInterceptor {

    public Object getInstance(Class claxx) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claxx);
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//        methodProxy.invokeSuper(o,objects);
        return method.getName();
    }
}


interface TestInterface
{
    String getHalloWorld();
}
