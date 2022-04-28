package entity;

import lombok.Data;

import java.io.Serializable;

//这些都是之前封装好传过去的
@Data
public class Hello implements Serializable {
    private Integer methodID;
    private String saying;
}
