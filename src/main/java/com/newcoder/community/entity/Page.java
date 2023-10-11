package com.newcoder.community.entity;

import java.util.Map;

/**
 * 分页信息
 */
public class Page {
    // 当前页码
    private Integer current = 1;
    // 显示上限
    private Integer limit = 10;
    // 数据总数
    private Integer rows;
    // 页面路径
    private String path;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (current != null && current >= 1) {
            this.current = current;
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if (limit != null && limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 起始行数
     * @return
     */
    public Integer getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 总页码
     * @return
     */
    public Integer getTotal() {
        return (rows + limit - 1) / limit;
    }

    /**
     * 起始页码
     * @return
     */
    public Integer getFrom() {
        return Math.max(1,  current - 2);
    }

    /**
     * 结束页码
     * @return
     */
    public Integer getTo() {
        return Math.min(this.getTotal(), current + 2);
    }
}
