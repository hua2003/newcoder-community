package com.newcoder.community.entity;

import java.util.Map;

/**
 * 分页信息
 */
public class Page {
    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数
    private int rows;
    // 页面路径
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
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
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 总页码
     * @return
     */
    public int getTotal() {
        return (rows + limit - 1) / limit;
    }

    /**
     * 起始页码
     * @return
     */
    public int getFrom() {
        return Math.max(1,  current - 2);
    }

    /**
     * 结束页码
     * @return
     */
    public int getTo() {
        return Math.min(this.getTotal(), current + 2);
    }
}
