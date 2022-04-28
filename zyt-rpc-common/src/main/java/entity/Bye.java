package entity;

import lombok.Data;

import java.io.Serializable;//jdk基本的序列化

//这些都是之前封装好传过去的
@Data
public class Bye implements Serializable {
    private Integer methodID;
    private String saying;
}
