package com.example.demo.entity;

import javax.xml.transform.Result;
import java.util.List;

public class PageInfo <T>{
    private List<T> list;       // 当前页数据
    private int pageNum;       // 当前页码
    private int pageSize;      // 每页大小
    private int total;         // 总记录数
    private int pages;         // 总页数
    // 无参构造器
    public PageInfo() {
    }

    // 全参构造器
    public PageInfo(List<T> list, int pageNum, int pageSize, int total, int pages) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
    }

    // getters 和 setters 方法

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
