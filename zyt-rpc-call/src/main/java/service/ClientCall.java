package service;

import annotation.RpcClientBootStrap;
import annotation.RpcToolsSelector;
import entity.Person;
import entity.PersonPOJO;
import method.Customer;
import service.call.ChosenClientCall;


//总客户端启动类  用户调用 什么版本的  和用什么工具 使用什么注册中心  序列化的选择 都可以用这个来玩
//注册中心不能给过去 这样就是重复依赖了
@RpcClientBootStrap(version = "1.2")
@RpcToolsSelector(rpcTool = "Nio")
public class ClientCall {
    public static void main(String[] args)  {
        //实现调用
        Customer customer = ChosenClientCall.start();

        // long start = System.currentTimeMillis();

        // System.out.println(customer.GetName(new Person("祝英台")));
        //
        // //测试 2.0版本之后
        // System.out.println(customer.GetPerson(new Person("zz")));
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
        //     System.out.println(customer1.GetPerson(new Person("zz")));
        // }).start();
        //
        // System.out.println(customer.GetName(new Person("zzz")));
        // System.out.println(customer.GetName(new Person("zzz")));
        // System.out.println(customer.GetName(new Person("zzz")));
        // System.out.println(customer.GetName(new Person("zzz")));

        //2.4版本之前的测试
        // System.out.println(customer.GetPerson(PersonPOJO.Person.newBuilder().setName("炸油条").build()));

        //测试
        // System.out.println(customer.GetName(PersonPOJO.Person.newBuilder().setName("炸油条").build()));


        // nio使用测试
        System.out.println(customer.Hello("success"));
        System.out.println(customer.Bye("fail"));
        System.out.println(customer.Bye("fail"));
        System.out.println(customer.Bye("fail"));


        // long end = System.currentTimeMillis();
        // System.out.println(end-start);

    }
}
