package edu.ustb.minddata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@ApiModel(value="Personnelrecord对象", description="")
public class Personnelrecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private String pid;

    private Integer record;

    private String start;

    private String end;

    private Integer tag;

    private Long unixstart;

    private Long unixend;


}
