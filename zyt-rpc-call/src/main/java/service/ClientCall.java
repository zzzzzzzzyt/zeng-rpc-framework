package service;

import annotation.RpcClientBootStrap;
import annotation.RpcToolsSelector;
import lombok.extern.slf4j.Slf4j;
import method.Customer;
import service.call.ChosenClientCall;


//总客户端启动类  用户调用 什么版本的  和用什么工具 使用什么注册中心  序列化的选择 都可以用这个来玩
//注册中心不能给过去 这样就是重复依赖了
/**
 * @author 祝英台炸油条
 */
@Slf4j
@RpcClientBootStrap(version = "1.2")
@RpcToolsSelector(rpcTool = "Nio")
public class ClientCall {
    public static void main(String[] args)  {
        //实现调用
        Customer customer = ChosenClientCall.start();

        // long start = System.currentTimeMillis();

        // log.info(customer.GetName(new Person("祝英台")));
        //
        // //测试 2.0版本之后
        // log.info(customer.GetPerson(new Person("zz")));
        // //
        // new Thread(()->{
        //     Customer customer1 = null;
        //     try {
        //         customer1 = ChosenClientCall.start();
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     } catch (RpcException e) {
        //         e.printStackTrace();
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        //     log.info(customer1.GetPerson(new Person("zz")));
        // }).start();
        //
        // log.info(customer.GetName(new Person("zzz")));
        // log.info(customer.GetName(new Person("zzz")));
        // log.info(customer.GetName(new Person("zzz")));
        // log.info(customer.GetName(new Person("zzz")));

        //2.4版本之前的测试
        // log.info(customer.GetPerson(PersonPOJO.Person.newBuilder().setName("炸油条").build()));

        //测试
        // log.info(customer.GetName(PersonPOJO.Person.newBuilder().setName("炸油条").build()));


        // nio使用测试
        log.info(customer.Hello("success"));
        log.info(customer.Bye("fail"));
        log.info(customer.Bye("fail"));
        log.info(customer.Bye("fail"));


        // long end = System.currentTimeMillis();
        // log.info(end-start);

    }
}
