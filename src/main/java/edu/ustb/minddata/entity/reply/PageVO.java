package edu.ustb.minddata.entity.reply;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * @author UmiSkky
 */
@Data
public class PageVO<T> {

    /**
     * 分页数据
     */
    private List<T> records;

    /**
     * 当前页索引
     */
    private Long current;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 总数据量
     */
    private Long total;

    /**
     * 每页大小
     */
    private Long size;

    public PageVO(Page<T> page) {
        this.records = page.getRecords();
        this.current = page.getCurrent();
        this.pages = page.getPages();
        this.total = page.getTotal();
        this.size = page.getSize();
    }

}
