package exception;



/**
 * 自定义异常
 */
public class RpcException extends Exception{
    public RpcException(){super();}
    public RpcException(String message){super(message);}
}
