package edu.ustb.minddata.service;

import java.util.List;

/**
 * @author UmiSkky
 */
public interface ReportService {

    /**
     * 查询某一测试的测试报告Url
     * @param rid String
     * @return List<String>
     * @throws Exception 异常
     */
    List<String> queryReportUrlByRid(String rid) throws Exception;

    /**
     * 根据rid删除对应测试报告的文件及文件夹
     * @param rid String
     * @return int
     * @throws Exception 异常
     */
    int deleteReportByRid(String rid) throws Exception;
}
