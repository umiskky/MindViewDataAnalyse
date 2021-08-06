package edu.ustb.minddata.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.ustb.minddata.entity.Personnel;
import edu.ustb.minddata.entity.reply.PageVO;
import edu.ustb.minddata.entity.request.PersonnelDTO;
import edu.ustb.minddata.exception.ResultBody;
import edu.ustb.minddata.service.PersonnelService;
import edu.ustb.minddata.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author UmiSkky
 */
@Slf4j(topic = "interface")
@RestController
@RequestMapping("/api")
public class PersonnelController {

    private final PersonnelService personnelService;

    @Autowired
    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    //========================================================================================

    @PostMapping("/personnel")
    public ResultBody addPersonnel(@RequestBody PersonnelDTO personnelDTO) throws Exception{
        return ResultBody.success(personnelService.insertSinglePersonnel(personnelDTO.createPersonnel()));
    }

    @DeleteMapping("/personnel")
    public ResultBody deletePersonnel(@RequestParam String number) throws Exception{
        return ResultBody.success(personnelService.deletePersonnelByNumber(number));
    }

    @PutMapping("/personnel")
    public ResultBody updatePersonnel(@RequestBody Personnel personnel) throws Exception{
        return ResultBody.success(personnelService.updatePersonnel(personnel));
    }

    //========================================================================================

    @PutMapping("/personnel/multi")
    public ResultBody addOrUpdateMultiPersonnel(@RequestBody List<PersonnelDTO> personnelDTOList) throws Exception{
        List<Personnel> personnelList = new ArrayList<>();
        personnelDTOList.forEach(personnelDTO -> {
            personnelList.add(personnelDTO.createPersonnel());
        });
        return ResultBody.success(personnelService.insertOrUpdateMultiPersonnel(personnelList));
    }

    @DeleteMapping("/personnel/multi")
    public ResultBody deleteMultiPersonnel(@RequestBody List<String> numberList) throws Exception{
        return ResultBody.success(personnelService.deleteMultiPersonnel(numberList));
    }

    @GetMapping("/personnel/multi")
    public ResultBody getAllPersonnel(@RequestParam(required = false, defaultValue = "1") String pageIndex,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception{
        Page<Personnel> page= new PageUtil<Personnel>(pageIndex, pageSize).getPage();
        personnelService.queryAllPersonnel(page);
        return ResultBody.success(new PageVO<>(page));
    }
}
