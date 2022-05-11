package method;

import entity.Person;
import entity.PersonPOJO;

//这是之后要被代理的对象 我们会实现它的方法
public interface Customer {
    public String Hello(String msg);
    public String Bye(String msg);

    //加个简单的先试试 传送person 获取他的姓名
    public String GetName(Person person);
    //传送protoc编译过的类
    public String GetName(PersonPOJO.Person personPOJO);

    public Person GetPerson(Person person);
    //传送protoc编译过的类
    public PersonPOJO.Person GetPerson(PersonPOJO.Person personPOJO);

}
