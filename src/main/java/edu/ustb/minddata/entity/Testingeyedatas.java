package edu.ustb.minddata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author umiskky
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Testingeyedatas对象", description="")
public class Testingeyedatas implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String recid;

    @TableField("`index`")
    private Integer index;

    private Integer gazetag;

    private String x;

    private String y;

    private String z;

    private String time;


}
