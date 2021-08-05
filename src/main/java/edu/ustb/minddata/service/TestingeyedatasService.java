package edu.ustb.minddata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ustb.minddata.entity.Personnelrecord;
import edu.ustb.minddata.entity.Testingeyedatas;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author umiskky
 * @since 2021-08-02
 */
public interface TestingeyedatasService extends IService<Testingeyedatas> {

    /**
     * 查询所有测试眼动数据
     * @param page IPage<Testingeyedatas>
     */
    void queryAllTestingEyeData(IPage<Testingeyedatas> page);
}
