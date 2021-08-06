package edu.ustb.minddata.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.ustb.minddata.entity.Testingeyedatas;
import edu.ustb.minddata.mapper.TestingeyedatasMapper;
import edu.ustb.minddata.service.TestingeyedatasService;
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
public class TestingeyedatasServiceImpl extends ServiceImpl<TestingeyedatasMapper, Testingeyedatas> implements TestingeyedatasService {

    private final TestingeyedatasMapper testingEyeDataMapper;

    @Autowired
    public TestingeyedatasServiceImpl(TestingeyedatasMapper testingEyeDataMapper) {
        this.testingEyeDataMapper = testingEyeDataMapper;
    }

    @Override
    public void queryAllTestingEyeData(IPage<Testingeyedatas> page) {
        testingEyeDataMapper.selectPage(page, null);
    }
}
