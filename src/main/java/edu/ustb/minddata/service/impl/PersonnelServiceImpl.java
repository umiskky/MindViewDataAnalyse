package edu.ustb.minddata.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ustb.minddata.entity.Personnel;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.exception.DefinedException;
import edu.ustb.minddata.mapper.PersonnelMapper;
import edu.ustb.minddata.service.PersonnelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class PersonnelServiceImpl extends ServiceImpl<PersonnelMapper, Personnel> implements PersonnelService {

    private final PersonnelMapper personnelMapper;

    @Autowired
    public PersonnelServiceImpl(PersonnelMapper personnelMapper) {
        this.personnelMapper = personnelMapper;
    }

    //========================================================================================
    // 单一数据的增删改查
    //========================================================================================

    @Override
    public int insertSinglePersonnel(Personnel personnel) throws Exception{
        personnel.setId(IdUtil.randomUUID());
        personnel.setTimestamp(System.currentTimeMillis());
        if(!this.isPersonnelValid(personnel)){
            log.error("[DataBase] Failed to insert a personnel, " + ResultEnum.PERSONNEL_INFO_INCOMPLETE.getResultMsg() + ":\n" + personnel);
            throw new DefinedException(ResultEnum.PERSONNEL_INFO_INCOMPLETE);
        }

        QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
        wrapper.eq("number", personnel.getNumber());
        Personnel tmp = personnelMapper.selectOne(wrapper);
        if(tmp != null){
            log.error("[DataBase] Failed to insert a personnel, " + ResultEnum.PERSONNEL_ALREADY_EXIST.getResultMsg() + ":\n" + tmp);
            throw new DefinedException(ResultEnum.PERSONNEL_ALREADY_EXIST);
        }
        log.info("[DataBase] Insert a personnel : " + personnel);

        int res = personnelMapper.insert(personnel);
        if(res != 1){
            log.error("[DataBase] Failed to insert a personnel, " + ResultEnum.PERSONNEL_INSERT_UNKNOWN_ERROR.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_INSERT_UNKNOWN_ERROR);
        }
        return res;
    }

    @Override
    public int deletePersonnelByNumber(String number) throws Exception {
        QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
        wrapper.eq("number", number);
        Personnel personnel = personnelMapper.selectOne(wrapper);
        if(personnel == null){
            log.error("[DataBase] Failed to delete a personnel, " + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }else{
            int res = personnelMapper.deleteById(personnel.getId());
            if(res == 1){
                log.info("[DataBase] Delete a personnel:" + personnel);
            }else{
                log.error("[DataBase] Failed to delete a personnel, " + ResultEnum.PERSONNEL_DELETE_UNKNOWN_ERROR.getResultMsg());
                throw new DefinedException(ResultEnum.PERSONNEL_DELETE_UNKNOWN_ERROR);
            }
            return res;
        }
    }

    @Override
    public int updatePersonnel(Personnel personnel) throws Exception {
        Personnel p = personnelMapper.selectById(personnel.getId());
        if(p == null){
            log.error("[DataBase] Failed to update a personnel, " + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }else{
            if(!p.getNumber().equals(personnel.getNumber())){
                log.error("[DataBase] Failed to update a personnel, " + ResultEnum.PERSONNEL_UPDATE_NUMBER_INVALID.getResultMsg());
                throw new DefinedException(ResultEnum.PERSONNEL_UPDATE_NUMBER_INVALID);
            }
            p.setAge(personnel.getAge()==null ? p.getAge() : personnel.getAge());
            p.setSex(personnel.getSex()==null ? p.getSex() : personnel.getSex());
            p.setNature(personnel.getNature()==null ? p.getNature() : personnel.getNature());
            p.setTimestamp(System.currentTimeMillis());
            int res = personnelMapper.updateById(p);
            if(res == 1){
                log.info("[DataBase] Update a personnel:" + p);
            }else{
                log.error("[DataBase] Failed to update a personnel, " + ResultEnum.PERSONNEL_UPDATE_UNKNOWN_ERROR.getResultMsg());
                throw new DefinedException(ResultEnum.PERSONNEL_UPDATE_UNKNOWN_ERROR);
            }
            return res;
        }
    }

    @Override
    public Personnel queryPersonnelByNumber(String number) throws Exception {
        QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
        wrapper.eq("number", number);
        Personnel personnel = personnelMapper.selectOne(wrapper);
        if(personnel == null){
            log.error("[DataBase] Failed to query a personnel, " + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }else{
            log.info("[DataBase] Query a personnel:" + personnel);
            return personnel;
        }
    }

    @Override
    public List<Personnel> queryPersonnelByName(String name) throws Exception {
        List<Personnel> personnelList = new ArrayList<>();

        // 参数校验
        if(StrUtil.isEmpty(name)){
            log.error("[DataBase] " + ResultEnum.BODY_NOT_MATCH.getResultMsg() + " name=" + name);
            throw new DefinedException(ResultEnum.BODY_NOT_MATCH);
        }

        // 进行模糊查询
        QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
        wrapper.like("nature", name);

        // 筛选符合条件结果
        ObjectMapper objectMapper = new ObjectMapper();
        for (Personnel personnel : personnelMapper.selectList(wrapper)) {
            Map<String, String> map = objectMapper.readValue(personnel.getNature(), new TypeReference<Map<String,String>>(){});
            String tmp = map.get("name");
            if(!StrUtil.isEmpty(tmp) && name.equals(tmp)){
                personnelList.add(personnel);
            }
        }

        if(personnelList.size()==0){
            log.error("[DataBase] Failed to query personnel, " + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }
        // 输出日志
        log.info("[DataBase] Query personnel: \n" + personnelList);
        return personnelList;
    }

    //========================================================================================
    // 批量数据的增删改查
    //========================================================================================

    @Override
    public int insertOrUpdateMultiPersonnel(List<Personnel> personnelList) throws Exception{

        // 检查被试者信息是否完整
        for (Personnel personnel : personnelList) {
            if(this.isPersonnelValid(personnel)){
                break;
            }else{
                log.error("[DataBase] Failed to insert or update personnel in batch, since personnel's information is incomplete: \n" + personnel);
                throw new DefinedException(ResultEnum.PERSONNEL_INFO_INCOMPLETE);
            }
        }

        int errorCount = 0;
        int updateCount = 0;
        int insertCount = 0;
        for (Personnel personnel : personnelList) {
            QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
            wrapper.eq("number", personnel.getNumber());
            Personnel tmp = personnelMapper.selectOne(wrapper);

            // 补全信息
            personnel.setTimestamp(System.currentTimeMillis());

            // 记录不存在,执行插入
            if(tmp == null){
                personnel.setId(IdUtil.randomUUID());
                int res = personnelMapper.insert(personnel);
                if(res == 1){
                    insertCount ++;
                }else if(res == 0){
                    log.error("[DataBase] An error occurred when insert personnel in batch: " + personnel);
                    errorCount ++;
                }
            }
            // 记录存在,执行更新
            else{
                tmp.setNumber(personnel.getNumber());
                tmp.setAge(personnel.getAge());
                tmp.setSex(personnel.getSex());
                tmp.setNature(personnel.getNature());
                tmp.setTimestamp(System.currentTimeMillis());
                int res = personnelMapper.updateById(tmp);
                if(res == 1){
                    updateCount ++;
                }else if(res == 0){
                    log.error("[DataBase] An error occurred when update personnel in batch: " + tmp);
                    errorCount ++;
                }
            }
        }

        log.info("[DataBase] Insert or update personnel in batch: "
                + " total: "
                + personnelList.size()
                + " success: "
                + (insertCount + updateCount)
                + " error: "
                + errorCount
                + " insert: "
                + insertCount
                + " update: "
                + updateCount);

        if(insertCount + updateCount != personnelList.size()){
            log.error("[DataBase] An error occurred when insert or update personnel in batch," + ResultEnum.PERSONNEL_INSERT_UPDATE_PART.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_INSERT_UPDATE_PART);
        }
        return insertCount + updateCount;
    }

    @Override
    public int insertMultiPersonnel(List<Personnel> personnelList) throws Exception{

        // 检查被试者信息是否完整
        for (Personnel personnel : personnelList) {
            if(this.isPersonnelValid(personnel)){
                break;
            }else{
                log.error("[DataBase] Failed to insert personnel in batch, since personnel's information is incomplete:\n" + personnel);
                throw new DefinedException(ResultEnum.PERSONNEL_INFO_INCOMPLETE);
            }
        }

        int res = personnelList.size();
        int alreadyExistError = 0;
        for (Personnel personnel : personnelList) {
            try {
                // TODO 日志可能出现大量插入未知错误记录
                this.insertSinglePersonnel(personnel);
            }catch (DefinedException e){
                if(ResultEnum.PERSONNEL_ALREADY_EXIST.getResultCode().equals(e.getResultCode())){
                    alreadyExistError ++;
                }
                res--;
            }
        }
        if(res != personnelList.size()){
            if(alreadyExistError + res == personnelList.size()){
                log.info("Insert personnel in batch: "
                        + " total:"
                        + personnelList.size()
                        + " success:"
                        + res
                        + " exist:"
                        + alreadyExistError);
            }else{
                log.error("[DataBase] Failed to insert personnel in batch" + ResultEnum.PERSONNEL_INSERT_UNKNOWN_ERROR.getResultMsg());
                throw new DefinedException(ResultEnum.PERSONNEL_INSERT_UNKNOWN_ERROR);
            }
        }
        return res;
    }

    @Override
    public int deleteMultiPersonnel(List<String> numbers) throws Exception {
        int nonexistenceCount = 0;
        int unknownCount = 0;
        int deleteCount = 0;

        for (String number : numbers) {
            QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
            wrapper.eq("number", number);
            Personnel personnel = personnelMapper.selectOne(wrapper);
            if(personnel == null){
                nonexistenceCount ++;
                log.error("[DataBase] An error occurred when delete personnel in batch, " + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg() + ",编号:" + number);
            }else{
                int tmp = personnelMapper.deleteById(personnel.getId());
                if(tmp == 1){
                    deleteCount ++;
                    log.info("[DataBase] Delete a personnel:" + personnel);
                }else if(tmp == 0){
                    unknownCount ++;
                    log.error("[DataBase] An error occurred when delete a personnel, " + ResultEnum.PERSONNEL_DELETE_UNKNOWN_ERROR.getResultMsg() + ",编号:" + number);
                }
            }
        }

        log.info("[DataBase] Delete personnel in batch: "
                + " total: "
                + numbers.size()
                + " success: "
                + deleteCount
                + " error: "
                + (nonexistenceCount + unknownCount)
                + " not found: "
                + nonexistenceCount
                + " unknown: "
                + unknownCount);

        if(deleteCount != numbers.size()){
            log.error("[DataBase] An error occurred when delete personnel in batch, " + ResultEnum.PERSONNEL_DELETE_PART.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_DELETE_PART);
        }
        return deleteCount;
    }

    @Override
    public void queryAllPersonnel(IPage<Personnel> page) throws Exception {
        personnelMapper.selectPage(page, null);
    }

    //========================================================================================
    //
    //========================================================================================

    /**
     * 检查被试者信息是否完整
     * @param personnel 被试者
     * @return boolean
     */
    private boolean isPersonnelValid(Personnel personnel){
        return !(personnel.getNumber()==null || "".equals(personnel.getNumber()));
    }
}
