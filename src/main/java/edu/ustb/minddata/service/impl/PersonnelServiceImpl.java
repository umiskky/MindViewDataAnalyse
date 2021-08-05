package edu.ustb.minddata.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ustb.minddata.entity.Personnel;
import edu.ustb.minddata.entity.Personnelrecord;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.exception.DefinedException;
import edu.ustb.minddata.mapper.PersonnelMapper;
import edu.ustb.minddata.service.PersonnelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            log.error("[DataBase] 插入一条被试者信息失败，" + ResultEnum.PERSONNEL_INFO_INCOMPLETE.getResultMsg() + "：\n" + personnel);
            throw new DefinedException(ResultEnum.PERSONNEL_INFO_INCOMPLETE);
        }

        Personnel tmp = null;
        QueryWrapper<Personnel> wrapper = new QueryWrapper<>();
        wrapper.eq("number", personnel.getNumber());
        tmp = personnelMapper.selectOne(wrapper);
        if(tmp != null){
            log.error("[DataBase] 插入一条被试者信息失败，" + ResultEnum.PERSONNEL_ALREADY_EXIST.getResultMsg() + "：\n" + tmp);
            throw new DefinedException(ResultEnum.PERSONNEL_ALREADY_EXIST);
        }
        log.info("[DataBase] 插入一条被试者记录: " + personnel);

        int res = personnelMapper.insert(personnel);
        if(res != 1){
            log.error("[DataBase] 插入被试者失败，" + ResultEnum.PERSONNEL_INSERT_UNKNOWN_ERROR.getResultMsg());
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
            log.error("[DataBase] 删除被试者失败，" + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }else{
            int res = personnelMapper.deleteById(personnel.getId());
            if(res == 1){
                log.info("[DataBase] 删除被试者：" + personnel);
            }else{
                log.error("[DataBase] 删除被试者失败，" + ResultEnum.PERSONNEL_DELETE_UNKNOWN_ERROR.getResultMsg());
                throw new DefinedException(ResultEnum.PERSONNEL_DELETE_UNKNOWN_ERROR);
            }
            return res;
        }
    }

    @Override
    public int updatePersonnel(Personnel personnel) throws Exception {
        Personnel p = personnelMapper.selectById(personnel.getId());
        if(p == null){
            log.error("[DataBase] 更新被试者失败，" + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }else{
            if(!p.getNumber().equals(personnel.getNumber())){
                log.error("[DataBase] 更新被试者失败，" + ResultEnum.PERSONNEL_UPDATE_NUMBER_INVALID.getResultMsg());
                throw new DefinedException(ResultEnum.PERSONNEL_UPDATE_NUMBER_INVALID);
            }
            p.setAge(personnel.getAge()==null ? p.getAge() : personnel.getAge());
            p.setSex(personnel.getSex()==null ? p.getSex() : personnel.getSex());
            p.setNature(personnel.getNature()==null ? p.getNature() : personnel.getNature());
            p.setTimestamp(System.currentTimeMillis());
            int res = personnelMapper.updateById(p);
            if(res == 1){
                log.info("[DataBase] 更新被试者：" + p);
            }else{
                log.error("[DataBase] 更新被试者失败，" + ResultEnum.PERSONNEL_UPDATE_UNKNOWN_ERROR.getResultMsg());
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
            log.error("[DataBase] 查询被试者失败，" + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg());
            throw new DefinedException(ResultEnum.PERSONNEL_NOT_FOUND);
        }else{
            log.info("[DataBase] 查询被试者：" + personnel);
            return personnel;
        }
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
                log.error("[DataBase] 批量插入更新被试者失败，被试者信息不完整：\n" + personnel);
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

            // 记录不存在，执行插入
            if(tmp == null){
                personnel.setId(IdUtil.randomUUID());
                int res = personnelMapper.insert(personnel);
                if(res == 1){
                    insertCount ++;
                }else if(res == 0){
                    log.error("[DataBase] 批量插入更新失败（插入）：" + personnel);
                    errorCount ++;
                }
            }
            // 记录存在，执行更新
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
                    log.error("[DataBase] 批量插入更新失败（更新）：" + tmp);
                    errorCount ++;
                }
            }
        }

        log.info("[DataBase] 批量插入更新被试者记录： "
                + " 总数："
                + personnelList.size()
                + " 成功："
                + (insertCount + updateCount)
                + " 错误："
                + errorCount
                + " 插入数："
                + insertCount
                + " 更新数："
                + updateCount);

        if(insertCount + updateCount != personnelList.size()){
            log.error("[DataBase] 批量插入或更新出现错误，" + ResultEnum.PERSONNEL_INSERT_UPDATE_PART.getResultMsg());
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
                log.error("[DataBase] 批量插入被试者失败，被试者信息不完整：\n" + personnel);
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
                log.info("插入被试者记录： "
                        + " 总数："
                        + personnelList.size()
                        + " 成功："
                        + res
                        + " 已存在记录："
                        + alreadyExistError);
            }else{
                log.error("[DataBase] 批量插入被试者失败，" + ResultEnum.PERSONNEL_INSERT_UNKNOWN_ERROR.getResultMsg());
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
                log.error("[DataBase] 批量删除被试者出错，" + ResultEnum.PERSONNEL_NOT_FOUND.getResultMsg() + "，编号：" + number);
            }else{
                int tmp = personnelMapper.deleteById(personnel.getId());
                if(tmp == 1){
                    deleteCount ++;
                    log.info("[DataBase] 删除被试者：" + personnel);
                }else if(tmp == 0){
                    unknownCount ++;
                    log.error("[DataBase] 批量删除被试者出错，" + ResultEnum.PERSONNEL_DELETE_UNKNOWN_ERROR.getResultMsg() + "，编号：" + number);
                }
            }
        }

        log.info("[DataBase] 批量删除被试者记录： "
                + " 总数："
                + numbers.size()
                + " 成功："
                + deleteCount
                + " 错误："
                + (nonexistenceCount + unknownCount)
                + " 不存在数："
                + nonexistenceCount
                + " 未知错误数："
                + unknownCount);

        if(deleteCount != numbers.size()){
            log.error("[DataBase] 批量删除出现出错，" + ResultEnum.PERSONNEL_DELETE_PART.getResultMsg());
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
