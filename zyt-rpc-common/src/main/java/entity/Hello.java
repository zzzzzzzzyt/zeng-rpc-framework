package entity;

import lombok.Data;

import java.io.Serializable;

//这些都是之前封装好传过去的

/**
 * @author 祝英台炸油条
 */
@Data
public class Hello implements Serializable {
    private Integer methodID;
    private String saying;
}
