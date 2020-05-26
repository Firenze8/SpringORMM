package com.learn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_PAGE_SIZE = 20;
    private int pageSize = DEFAULT_PAGE_SIZE; //page count
    private long start; //the first record of current page, from 0
    private List<T> rows; //the records int current page, List type
    private long total;//all records
    public Page(){
        this(0,0, DEFAULT_PAGE_SIZE, new ArrayList<T>());
    }

    public Page(long start, long totalSize, int pageSize, List<T> rows){
        this.pageSize = pageSize;
        this.start = start;
        this.total = totalSize;
        this.rows = rows;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPageCount(){
        if(total%pageSize == 0){
            return  total/pageSize;
        }else{
            return  total/pageSize+1;
        }
    }

    public long getPageNo(){
        return start / pageSize + 1;
    }

    public boolean hasNextPage(){
        return this.getPageNo() < this.getTotalPageCount() - 1;
    }

    public boolean hasPreviousPage(){
        return this.getPageNo() > 1;
    }

    protected static int getStartOfPage(int pageNo){
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    public static int getStartOfPage(int pageNo, int pageSize){
        return (pageNo - 1)*pageSize;
    }
}
