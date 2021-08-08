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
    SUCCESS(200, "Success!"),
    BODY_NOT_MATCH(400,"The requested data format is inconsistent!"),
    NOT_FOUND(404, "Resource not found!"),
    INTERNAL_SERVER_ERROR(500, "Server internal error!"),
    SERVER_BUSY(503,"The server is busy, please try again later!"),
    TEST(-1,"Test!"),

    PERSONNEL_INFO_INCOMPLETE(1001, "Personnel's information is incomplete!"),
    PERSONNEL_ALREADY_EXIST(1002, "Personnel already exist!"),
    PERSONNEL_INSERT_UNKNOWN_ERROR(1003, "An unknown error occurred when insert personnel!"),
    PERSONNEL_INSERT_UPDATE_PART(1004, "Insert or delete personnel in batch success partly!"),

    PERSONNEL_NOT_FOUND(1005, "Personnel not found!"),
    PERSONNEL_DELETE_UNKNOWN_ERROR(1006, "An unknown error occurred when delete personnel!"),
    PERSONNEL_DELETE_PART(1007, "Delete personnel in batch success partly!"),

    PERSONNEL_UPDATE_NUMBER_INVALID(1008, "The number does not match the ID, the number can not be changed!"),
    PERSONNEL_UPDATE_UNKNOWN_ERROR(1009, "An unknown error occurred when update personnel!"),

    PERSONNEL_RECORD_NOT_FOUND(2001, "Personnel record not found!"),

    REPORT_NOT_EXIST(3001, "Report not exist!"),
    REPORT_CORRUPTED(3002, "Report is corrupted!"),

    DATASOURCE_CREATE_ERROR(4001, "Failed to add a data source!"),
    DATASOURCE_SWITCH_ERROR(4002, "Failed to switch a data source!"),

    FILE_UPLOAD_TYPE_NOT_MATCHED(5001, "File type incorrect!"),
    FILE_UPLOAD_FAILED(5002, "File upload failed with an unknown error!"),
    FILE_UPLOAD_NOT_FOUND(5003, "File is not found in upload buffer!"),
    TABLE_HEAD_REDUNDANT(5004, "Table head may have redundant fields!"),
    TABLE_HEAD_NOT_INITIALIZE(5005, "Table head miss some required fields! Required:[number, gender, age, name]！")
    ;

    private Integer resultCode;

    private String resultMsg;
}
