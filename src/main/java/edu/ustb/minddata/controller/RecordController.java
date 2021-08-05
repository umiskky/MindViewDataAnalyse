package edu.ustb.minddata.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.ustb.minddata.config.datasource.SwitchDataSource;
import edu.ustb.minddata.entity.Personnelrecord;
import edu.ustb.minddata.entity.Testingeyedatas;
import edu.ustb.minddata.entity.Testingtimedatas;
import edu.ustb.minddata.entity.reply.PageVO;
import edu.ustb.minddata.exception.ResultBody;
import edu.ustb.minddata.service.PersonnelrecordService;
import edu.ustb.minddata.service.TestingeyedatasService;
import edu.ustb.minddata.service.TestingtimedatasService;
import edu.ustb.minddata.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author UmiSkky
 */
@Slf4j(topic = "interface")
@RestController
@RequestMapping("/api")
public class RecordController {

    private final PersonnelrecordService personnelRecordService;

    private final TestingtimedatasService testingTimeDataService;

    private final TestingeyedatasService testingEyeDataService;


    @Autowired
    public RecordController(PersonnelrecordService personnelRecordService, TestingtimedatasService testingTimeDataService, TestingeyedatasService testingEyeDataService) {
        this.personnelRecordService = personnelRecordService;
        this.testingTimeDataService = testingTimeDataService;
        this.testingEyeDataService = testingEyeDataService;
    }

    @GetMapping("/record/personnel")
    public ResultBody getPersonnelRecordByPid(@RequestParam String pid,
                                              @RequestParam(required = false, defaultValue = "1") String pageIndex,
                                              @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception{
        Page<Personnelrecord> page= new PageUtil<Personnelrecord>(pageIndex, pageSize).getPage();
        personnelRecordService.queryPersonnelRecordByPid(page, pid);
        return ResultBody.success(new PageVO<>(page));
    }

    @GetMapping("/record/time")
    @SwitchDataSource
    public ResultBody getTimeDataByRid(@RequestParam String rid,
                                       @RequestParam(required = false, defaultValue = "1") String pageIndex,
                                       @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception{
        Page<Testingtimedatas> page= new PageUtil<Testingtimedatas>(pageIndex, pageSize).getPage();
        testingTimeDataService.queryAllTestingTimeData(page);
        return ResultBody.success(new PageVO<>(page));
    }

    @GetMapping("/record/eye")
    @SwitchDataSource
    public ResultBody getEyeDataByRid(@RequestParam String rid,
                                      @RequestParam(required = false, defaultValue = "1") String pageIndex,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception{
        Page<Testingeyedatas> page= new PageUtil<Testingeyedatas>(pageIndex, pageSize).getPage();
        testingEyeDataService.queryAllTestingEyeData(page);
        return ResultBody.success(new PageVO<>(page));
    }
}
