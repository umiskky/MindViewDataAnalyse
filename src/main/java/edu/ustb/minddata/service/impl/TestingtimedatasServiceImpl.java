package edu.ustb.minddata.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.ustb.minddata.entity.Testingtimedatas;
import edu.ustb.minddata.mapper.TestingtimedatasMapper;
import edu.ustb.minddata.service.TestingtimedatasService;
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
public class TestingtimedatasServiceImpl extends ServiceImpl<TestingtimedatasMapper, Testingtimedatas> implements TestingtimedatasService {

    private final TestingtimedatasMapper testingTimeDataMapper;

    @Autowired
    public TestingtimedatasServiceImpl(TestingtimedatasMapper testingTimeDataMapper) {
        this.testingTimeDataMapper = testingTimeDataMapper;
    }

    @Override
    public void queryAllTestingTimeData(IPage<Testingtimedatas> page) {
        testingTimeDataMapper.selectPage(page, null);
    }
}
