package edu.ustb.minddata.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.ustb.minddata.config.PathConfig;
import edu.ustb.minddata.entity.Personnel;
import edu.ustb.minddata.entity.Personnelrecord;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.exception.DefinedException;
import edu.ustb.minddata.mapper.PersonnelMapper;
import edu.ustb.minddata.mapper.PersonnelrecordMapper;
import edu.ustb.minddata.service.PersonnelrecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

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

    private final PersonnelMapper personnelMapper;

    @Autowired
    public PersonnelrecordServiceImpl(PersonnelrecordMapper personnelrecordMapper, PersonnelMapper personnelMapper) {
        this.personnelrecordMapper = personnelrecordMapper;
        this.personnelMapper = personnelMapper;
    }

    @Override
    public void queryPersonnelRecord(IPage<Personnelrecord> page) throws Exception{
        personnelrecordMapper.selectPage(page, null);
    }

    @Override
    public int deletePersonnelRecordByRid(String rid) throws Exception {
        int res = personnelrecordMapper.deleteById(rid);
        if(res==1){
            log.info("[Database] Delete personnel record: " + rid);
        }
        return res;
    }

    @Override
    public int deletePersonnelRecordDataByRid(String rid) throws Exception {
        File dbFile = Paths.get(PathConfig.getPrefixPath(), (rid + ".db")).toAbsolutePath().toFile();
        File txtFile = Paths.get(PathConfig.getPrefixPath(), (rid + ".txt")).toAbsolutePath().toFile();
        int res = 0;

        if(FileUtil.isFile(dbFile)){
            System.gc();
            boolean tmp = FileUtil.del(dbFile);
            if(tmp){
                log.info("[File] Delete database file: " + dbFile.getAbsolutePath());
            }
            res += tmp ? 1 : 0;
        }

        if(FileUtil.isFile(txtFile)){
            System.gc();
            boolean tmp = FileUtil.del(txtFile);
            if(tmp){
                log.info("[File] Delete txt file(data buffer): " + txtFile.getAbsolutePath());
            }
            res += tmp ? 1 : 0;
        }
        return res;
    }

    @Override
    public List<Personnelrecord> queryPersonnelRecordByPid(String pid) throws Exception{
        QueryWrapper<Personnelrecord> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", pid);
        List<Personnelrecord> personnelRecordList = personnelrecordMapper.selectList(wrapper);
        if(personnelRecordList.size() == 0){
            log.error("[Database] " + ResultEnum.PERSONNEL_RECORD_NOT_FOUND.getResultMsg() + " pid=" + pid);
            throw new DefinedException(ResultEnum.PERSONNEL_RECORD_NOT_FOUND);
        }
        return personnelRecordList;
    }

    @Override
    public List<Personnelrecord> queryPersonnelRecordByNumber(String number) throws Exception {
        QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
        wrapper.eq("number", number).select("id");
        Personnel personnel = personnelMapper.selectOne(wrapper);
        if(personnel==null || StrUtil.isEmpty(personnel.getId())){
            log.error("[Database] " + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg() + " number=" + number);
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }
        return queryPersonnelRecordByPid(personnel.getId());
    }
}
