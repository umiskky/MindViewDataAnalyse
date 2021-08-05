package edu.ustb.minddata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.ustb.minddata.entity.Personnelrecord;

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
     * @param pid String
     */
    void queryPersonnelRecordByPid(IPage<Personnelrecord> page, String pid);


}
