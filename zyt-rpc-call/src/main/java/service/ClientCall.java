package service;

import annotation.RpcClientBootStrap;
import annotation.RpcToolsSelector;
import entity.Person;
import lombok.extern.slf4j.Slf4j;
import method.Customer;
import service.call.ChosenClientCall;


//总客户端启动类  用户调用 什么版本的  和用什么工具 使用什么注册中心  序列化的选择 都可以用这个来玩
//注册中心不能给过去 这样就是重复依赖了

/**
 * @author 祝英台炸油条
 */
@Slf4j
@RpcClientBootStrap(version = "2.4")
@RpcToolsSelector(rpcTool = "Netty")
public class ClientCall {
    public static void main(String[] args) {
        //实现调用
        Customer customer = ChosenClientCall.start();
        assert customer != null;

        // long start = System.currentTimeMillis();
        // log.info(customer.GetName(new Person("祝英台")));
        //
        //测试 2.0版本之后
        Person person1 = customer.GetPerson(new Person("zz"));
        String msg1 = "获取对应类" + person1.getClass() + "，名字为" + person1.getName();
        log.info(msg1);
        //
        new Thread(() -> {
            Customer customer1 = ChosenClientCall.start();
            Person person2 = customer1.GetPerson(new Person("zz"));
            String msg2 = "获取对应类" + person2.getClass() + "，名字为" + person2.getName();
            log.info(msg2);
        }).start();

        log.info(customer.GetName(new Person("zzz")));
        log.info(customer.GetName(new Person("zzz")));
        log.info(customer.GetName(new Person("zzz")));
        log.info(customer.GetName(new Person("zzz")));
        //2.4版本之前的测试
        // log.info(customer.GetPerson(PersonPOJO.Person.newBuilder().setName("炸油条").build()));
        //测试
        // log.info(customer.GetName(PersonPOJO.Person.newBuilder().setName("炸油条").build()));


        // nio使用测试  netty也可以使用
        log.info(customer.Hello("success"));
        log.info(customer.Bye("fail"));
        log.info(customer.Bye("fail"));
        log.info(customer.Bye("fail"));


        // long end = System.currentTimeMillis();
        // log.info(end-start);

    }
}
