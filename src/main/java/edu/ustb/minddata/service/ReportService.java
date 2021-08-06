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
}
