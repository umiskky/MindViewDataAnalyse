package edu.ustb.minddata.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author UmiSkky
 */
public interface PersonnelExcelService {

    /**
     * 向服务器上传excel文件
     * @param multipartFile 传入的文件
     * @return String
     * @throws Exception 异常
     */
    String uploadExcelFile(MultipartFile multipartFile) throws Exception;

    /**
     * 根据excel文件批量添加Personnel
     * @param excelFileName String
     * @return Map<String, String>
     * @throws Exception 异常
     */
    Map<String, Object> insertPersonnelByExcelFile(String excelFileName) throws Exception;
}
