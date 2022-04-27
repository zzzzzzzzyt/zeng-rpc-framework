package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//网络传输请求
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    //方法编号
    int methodNum;
    //消息体
    String message;
}
