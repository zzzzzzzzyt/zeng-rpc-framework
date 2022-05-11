package consumer.proxy.netty;

import annotation.RegistryChosen;
import consumer.netty.NettyClient;
import consumer.proxy.ClientProxy;
import consumer.service_discovery.NacosServiceDiscovery;
import consumer.service_discovery.ZkCuratorDiscovery;
import consumer.service_discovery.ZkServiceDiscovery;
import exception.RpcException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import register.Register;

import java.lang.reflect.Method;

//Cglib实现代理模式
public class RpcNettyClientCGLIBProxy implements ClientProxy,MethodInterceptor{

    @Override
    public Object getBean(Class serviceClass) {
        //设置动态代理增强类
        Enhancer enhancer = new Enhancer();
        //设置类加载器
        // enhancer.setClassLoader(serviceClass.getClassLoader());
        //设置代理类
        enhancer.setSuperclass(serviceClass);
        //设置对应的方法执行拦截器，方法回调
        enhancer.setCallback(this);

        return enhancer.create();
    }

    /**
     * @param obj           代理对象（增强的对象）
     * @param method      被拦截的方法（需要增强的方法）
     * @param args        方法入参
     * @param methodProxy 用于调用原始方法
     */
    @Override //自定义对应的拦截 拦截方法并执行别的任务
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String methodAddress = getMethodAddress(method.getName());
        String[] split = methodAddress.split(":");
        return NettyClient.callMethod(split[0],Integer.valueOf(split[1]),args[0],method);
    }




    /**
     * 实际去获得对应的服务 并完成方法调用的方法
     * @param methodName  根据方法名  根据添加的注册中心注解来选择相应的注册中心进行  实现负载均衡获取一个方法对应地址
     * @param
     * @return
     */
    private static String getMethodAddress(String methodName) throws Exception {
        //根据注解进行方法调用
        //根据在代理类上的注解调用  看清楚底下的因为是个class数组 可以直接继续获取 注解
        RegistryChosen annotation = Register.class.getAnnotation(RegistryChosen.class);
        switch (annotation.registryName())
        {
            case "nacos":
                return NacosServiceDiscovery.getMethodAddress(methodName);
            case "zookeeper":
                return ZkServiceDiscovery.getMethodAddress(methodName);
            case "zkCurator":
                return ZkCuratorDiscovery.getMethodAddress(methodName);
            default:
                throw new RpcException("不存在该注册中心");
        }
    }
}
