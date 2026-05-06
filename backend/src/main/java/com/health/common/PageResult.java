package com.health.common;

import lombok.Data;
import java.util.List;

/**
 * 分页结果类
 */
@Data
public class PageResult<T> {
    
    private List<T> records;
    private Long total;
    private Long current;
    private Long size;
    private Long pages;
    
    public PageResult() {}
    
    public PageResult(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = size > 0 ? (total + size - 1) / size : 0;
    }
    
    /**
     * 静态工厂方法
     */
    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(records);
        result.setTotal(total);
        result.setCurrent((long) page);
        result.setSize((long) size);
        result.setPages(size > 0 ? (total + size - 1) / size : 0);
        return result;
    }
}
