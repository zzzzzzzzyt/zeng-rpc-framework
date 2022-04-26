package entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Hello implements Serializable {
    private Integer methodID;
    private String saying;
}
