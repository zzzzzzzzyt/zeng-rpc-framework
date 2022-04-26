package entity;

import lombok.Data;

import java.io.Serializable;//jdk基本的序列化

@Data
public class Bye implements Serializable {
    private Integer methodID;
    private String saying;
}
