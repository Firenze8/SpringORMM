package com.learn.crud;

public interface UpdateTop<T> {
    /**
     * 修改一条记录
     * @param entity
     * @return
     * @throws Exception
     */
    boolean update(T entity) throws Exception;
}
