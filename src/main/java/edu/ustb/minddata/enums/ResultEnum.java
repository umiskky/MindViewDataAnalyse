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
    TEST(-1,"Test!"),
    SUCCESS(200, "Success!"),
    BODY_NOT_MATCH(400,"The requested data format is inconsistent!"),
    NOT_FOUND(404, "Resource not found!"),
    INTERNAL_SERVER_ERROR(500, "Server internal error!"),
    SERVER_BUSY(503,"The server is busy, please try again later!"),

    DATASOURCE_CREATE_ERROR(1001, "Failed to add a data source!"),
    DATASOURCE_SWITCH_ERROR(1002, "Failed to switch a data source!"),

    PERSONNEL_INFO_INCOMPLETE(10001, "Personnel's information is incomplete!"),
    PERSONNEL_NOT_FOUND(10003, "Personnel not found!"),
    PERSONNEL_ALREADY_EXIST(10004, "Personnel already exist!"),
    PERSONNEL_INSERT_UPDATE_PART(10005, "Insert or delete personnel in batch success partly!"),
    PERSONNEL_DELETE_PART(10006, "Delete personnel in batch success partly!"),
    PERSONNEL_UPDATE_NUMBER_INVALID(10007, "The number does not match the ID, the number can not be changed!"),

    PERSONNEL_INSERT_UNKNOWN_ERROR(20001, "An unknown error occurred when insert personnel!"),
    PERSONNEL_DELETE_UNKNOWN_ERROR(20002, "An unknown error occurred when delete personnel!"),
    PERSONNEL_UPDATE_UNKNOWN_ERROR(20003, "An unknown error occurred when update personnel!"),

    REPORT_NOT_EXIST(30001, "Report not exist!"),
    REPORT_CORRUPTED(30002, "Report is corrupted!"),

    FILE_UPLOAD_TYPE_NOT_MATCHED(40001, "File type incorrect!"),
    FILE_UPLOAD_FAILED(40002, "File upload failed with an unknown error!"),
    FILE_UPLOAD_NOT_FOUND(40003, "File is not found in upload buffer!"),

    TABLE_HEAD_REDUNDANT(50001, "Table head may have redundant fields!"),
    TABLE_HEAD_NOT_INITIALIZE(50002, "Table head miss some required fields! Required:[number, gender, age, name]！")
    ;

    private Integer resultCode;

    private String resultMsg;
}
