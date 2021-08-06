package edu.ustb.minddata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@ApiModel(value="Testingtimedatas对象", description="")
public class Testingtimedatas implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String recid;

    @TableField("`index`")
    private Integer index;

    private String picture1;

    private String picture2;

    private String picture3;

    private String picskip;

    private String start;

    private Integer showtag;

    private Integer showpicture;

    private Integer showpicone;

    private Integer showpictwo;


}
