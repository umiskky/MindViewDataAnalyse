package edu.ustb.minddata.controller;

import edu.ustb.minddata.exception.ResultBody;
import edu.ustb.minddata.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author UmiSkky
 */
@Slf4j(topic = "interface")
@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report")
    public ResultBody getReportByRid(@RequestParam String rid) throws Exception{
        return ResultBody.success(reportService.queryReportUrlByRid(rid));
    }

    @DeleteMapping("/report")
    public ResultBody deleteReportByRid(@RequestParam String rid) throws Exception{
        return ResultBody.success(reportService.deleteReportByRid(rid));
    }
}
