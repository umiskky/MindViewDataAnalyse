package edu.ustb.minddata.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.ustb.minddata.entity.Personnel;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.exception.DefinedException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author UmiSkky
 */
@Slf4j(topic = "interface")
public class PageUtil<T> {

    @Getter
    private final Page<T> page;

    public PageUtil(String pageIndex, String pageSize) {
        int index=1,size=10;
        try{
            index = Integer.parseInt(pageIndex)<=0 ? 1 : Integer.parseInt(pageIndex);
            size = Integer.parseInt(pageSize)<=0 ? 10: Integer.parseInt(pageSize);
        }catch(NumberFormatException e){
            log.error(ResultEnum.BODY_NOT_MATCH.getResultMsg());
        }
        page = new Page<>(index, size);
    }
}
