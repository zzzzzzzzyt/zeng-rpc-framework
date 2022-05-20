package exception;



/**
 * 自定义异常
 * 目前来说我们的异常 还是比较简单的不需要去定义太多的东西  什么状态码之类的
 */
public class RpcException extends Exception{
    public RpcException(){super();}
    public RpcException(String message){super(message);}
}
