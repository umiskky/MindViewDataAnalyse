package edu.ustb.minddata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.ustb.minddata.entity.Personnelrecord;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author umiskky
 * @since 2021-08-02
 */
public interface PersonnelrecordService extends IService<Personnelrecord> {

    /**
     * 查询某一被试者的所有记录
     * @param page IPage<Personnelrecord>
     * @throws Exception 异常
     */
    void queryPersonnelRecord(IPage<Personnelrecord> page) throws Exception;

    /**
     * 查询所有pid对应的被试者记录
     * @param pid String
     * @return List<Personnelrecord>
     * @throws Exception 异常
     */
    List<Personnelrecord> queryPersonnelRecordByPid(String pid) throws Exception;


    /**
     * 查询所有number对应的被试者记录
     * @param number String
     * @return List<Personnelrecord>
     * @throws Exception 异常
     */
    List<Personnelrecord> queryPersonnelRecordByNumber(String number) throws Exception;
}
