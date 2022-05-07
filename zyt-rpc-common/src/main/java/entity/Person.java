package entity;


import java.io.Serializable;

//这是普通进行序列化传递需要
//千万注意要实现序列化接口

//接口一定要实现正确
public class Person implements Serializable {
    private String name;

    //构造方法
    public  Person(String name)
    {
        this.name = name;
    }
    public  Person() {}

    public String getName()
    {
        return name;
    }

}
