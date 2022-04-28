package method;

//这是之后要被代理的对象 我们会实现它的方法
public interface Customer {
    String hello(String msg);
    String bye(String msg);
}
