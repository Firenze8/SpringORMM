package com.learn.crud;

import java.util.List;

public interface InsertTop<T, PK> {
    /**
     * 插入一条记录并返回插入后的ID
     * @param entity
     * @return
     * @throws Exception
     */
    PK insertAndReturnId(T entity) throws Exception;

    /**
     * 插入一条记录自增ID
     * @param entity
     * @return
     * @throws Exception
     */
    boolean insert(T entity) throws Exception;

    /**
     * 批量插入
     * @param list
     * @return
     * @throws Exception
     */
    int insertAll(List<T> list) throws Exception;

}
