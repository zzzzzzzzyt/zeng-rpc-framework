package service.nio_bootstrap;

import annotation.RpcClientBootStrap;
import consumer.bootstrap.nio.NIOConsumerBootStrap12;
import consumer.bootstrap.nio.NIOConsumerBootStrap14;
import consumer.bootstrap.nio.NIOConsumerBootStrap15;
import exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import method.Customer;
import service.ClientCall;


//之后启动直接在这边启动根据 在注解中配置对应的版本号  将相应的操作封装到之后的操作中即可  这样很方便 就是每次咱加一个启动器还得改下switch
//比如说这里的version 1.2 就是v1.2版本的启动器

/**
 * @author 祝英台炸油条
 */
@Slf4j
public class NIOClientBootStrap {
    public static Customer start() {
        //获取当前的注解上的版本然后去调用相应的远端方法  反射的方法
        //当前客户端启动器class对象
        RpcClientBootStrap annotation = ClientCall.class.getAnnotation(RpcClientBootStrap.class);
        String currentVersion = annotation.version();
        //根据注解获得的版本进行判断是哪个版本 然后进行启动
        switch (currentVersion) {
            //1.2版本之前都是键盘输入 所以不是根据代理对象来进行调用的  暂时注释掉
            // case "1.0":
            //     NIOConsumerBootStrap10.main(null);
            //     break;
            // case "1.1":
            //     NIOConsumerBootStrap11.main(null);
            //     break;
            case "1.2":
                return NIOConsumerBootStrap12.main(null);
            case "1.4":
                return NIOConsumerBootStrap14.main(null);
            case "1.5":
                return NIOConsumerBootStrap15.main(null);
            default:
                try {
                    throw new RpcException("太着急了兄弟，这个版本还没出呢！要不你给我提个PR");
                } catch (RpcException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
        }
    }
}
