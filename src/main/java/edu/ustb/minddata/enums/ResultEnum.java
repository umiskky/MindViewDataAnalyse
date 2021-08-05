package edu.ustb.minddata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author UmiSkky
 */

@AllArgsConstructor
@Getter
public enum ResultEnum implements StatusInfoInterface {

    /**
     *
     */
    TEST(-1,"测试"),
    SUCCESS(200, "成功!"),
    BODY_NOT_MATCH(400,"请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(401,"请求的数字签名不匹配!"),
    NOT_FOUND(404, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
    SERVER_BUSY(503,"服务器正忙，请稍后再试!"),

    DATASOURCE_CREATE_ERROR(1001, "添加数据源失败"),
    DATASOURCE_SWITCH_ERROR(1002, "切换数据源失败"),

    PERSONNEL_INFO_INCOMPLETE(10001, "被试者信息不完整"),
    PERSONNEL_NOT_FOUND(10003, "未找到对应被试者信息"),
    PERSONNEL_ALREADY_EXIST(10004, "插入的被试者信息已存在"),
    PERSONNEL_INSERT_UPDATE_PART(10005, "批量插入或更新被试者部分成功"),
    PERSONNEL_DELETE_PART(10006, "批量删除被试者部分成功"),
    PERSONNEL_UPDATE_NUMBER_INVALID(10007, "编号与id不匹配，不能修改编号"),

    PERSONNEL_INSERT_UNKNOWN_ERROR(20001, "插入数据出现未知错误"),
    PERSONNEL_DELETE_UNKNOWN_ERROR(20002, "删除数据出现未知错误"),
    PERSONNEL_UPDATE_UNKNOWN_ERROR(20003, "更新数据出现未知错误")
    ;

    private Integer resultCode;

    private String resultMsg;
}
