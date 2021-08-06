package edu.ustb.minddata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.ustb.minddata.entity.Testingtimedatas;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author umiskky
 * @since 2021-08-02
 */
public interface TestingtimedatasService extends IService<Testingtimedatas> {

    /**
     * 查询所有测试时间数据
     * @param page IPage<Testingtimedatas>
     */
    void queryAllTestingTimeData(IPage<Testingtimedatas> page);
}
