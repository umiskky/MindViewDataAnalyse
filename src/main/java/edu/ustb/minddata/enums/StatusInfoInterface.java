package edu.ustb.minddata.enums;

/**
 * @author UmiSkky
 */
public interface StatusInfoInterface {

    /**
     * 错误码
     * @return String
     */
    Integer getResultCode();

    /**
     * 错误描述
     * @return String
     */
    String getResultMsg();
}
