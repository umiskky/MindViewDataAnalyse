package edu.ustb.minddata.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.ustb.minddata.entity.Personnel;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author umiskky
 * @since 2021-08-02
 */
public interface PersonnelService extends IService<Personnel> {

    //========================================================================================

    /**
     * 插入单个Personnel
     * @param personnel 被试者
     * @return int
     * @throws Exception 异常
     */
    int insertSinglePersonnel(Personnel personnel) throws Exception;

    /**
     * 根据编号删除被试者
     * @param number 编号
     * @return int
     * @throws Exception 异常
     */
    int deletePersonnelByNumber(String number) throws Exception;

    /**
     * 更新被试者信息
     * @param personnel 被试者
     * @return int
     * @throws Exception 异常
     */
    int updatePersonnel(Personnel personnel) throws Exception;

    /**
     * 根据编号查询Personnel
     * @param number 编号
     * @return Personnel
     * @throws Exception 异常
     */
    Personnel queryPersonnelByNumber(String number) throws Exception;

    /**
     * 根据姓名查询Personnel
     * @param name 姓名
     * @return List<Personnel>
     * @throws Exception 异常
     */
    List<Personnel> queryPersonnelByName(String name) throws Exception;

    //========================================================================================

    /**
     * 插入或更新多个Personnel
     * @param personnelList 被试者列表
     * @return int
     * @throws Exception 异常
     */
    int insertOrUpdateMultiPersonnel(List<Personnel> personnelList)  throws Exception;

    /**
     * 插入多个Personnel
     * @param personnelList 被试者列表
     * @return int
     * @throws Exception 异常
     */
    int insertMultiPersonnel(List<Personnel> personnelList)  throws Exception;

    /**
     * 删除多条被试者信息
     * @param numbers 编号列表
     * @return int
     * @throws Exception 异常
     */
    int deleteMultiPersonnel(List<String> numbers) throws Exception;

    /**
     * 获得所有被试者信息
     * @return List<Personnel>
     * @throws Exception 异常
     */
    void queryAllPersonnel(IPage<Personnel> page) throws Exception;
}
