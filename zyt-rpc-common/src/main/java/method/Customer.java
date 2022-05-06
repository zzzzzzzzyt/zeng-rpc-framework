package method;

import serialization.entity.Person;

//这是之后要被代理的对象 我们会实现它的方法
public interface Customer {
    String Hello(String msg);
    String Bye(String msg);
    //加个简单的先试试 传送person 获取他的姓名
    String GetName(Person person);
    Person GetPerson(Person person);
}
