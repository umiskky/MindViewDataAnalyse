package edu.ustb.minddata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.ustb.minddata.entity.Personnelrecord;
import edu.ustb.minddata.mapper.PersonnelrecordMapper;
import edu.ustb.minddata.service.PersonnelrecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author umiskky
 * @since 2021-08-02
 */
@Slf4j(topic = "database")
@Service
public class PersonnelrecordServiceImpl extends ServiceImpl<PersonnelrecordMapper, Personnelrecord> implements PersonnelrecordService {


    private final PersonnelrecordMapper personnelrecordMapper;

    @Autowired
    public PersonnelrecordServiceImpl(PersonnelrecordMapper personnelrecordMapper) {
        this.personnelrecordMapper = personnelrecordMapper;
    }

    @Override
    public void queryPersonnelRecordByPid(IPage<Personnelrecord> page, String pid) {
        QueryWrapper<Personnelrecord> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", pid);
        personnelrecordMapper.selectPage(page, wrapper);
    }
}
