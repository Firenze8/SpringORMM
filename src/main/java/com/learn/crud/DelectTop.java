package com.learn.crud;

import java.util.List;

public interface DelectTop<T> {
    /**
     * 删除一条记录
     * @param entity
     * @return
     * @throws Exception
     */
    boolean delete(T entity) throws Exception;

    /**
     * 批量删除
     * @param list
     * @return
     * @throws Exception
     */
    int deleteAll(List<T> list) throws Exception;
}
