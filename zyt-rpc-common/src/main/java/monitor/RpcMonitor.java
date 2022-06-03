package monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName rpc_monitor
 */
@TableName(value ="rpc_monitor")
@Data
public class RpcMonitor implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法描述
     */
    private String methodDescription;

    /**
     * 状态 0-正常
     */
    private Integer callNum;

    /**
     * 更新时间
     */
    private Date callTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}