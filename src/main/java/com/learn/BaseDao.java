package com.learn;

import java.util.List;
import java.util.Map;

public interface BaseDao<T, PK> {
    /**
     * query condition
     * @param queryRule
     * @return
     * @throws Exception
     */
    List<T> select(QueryRule queryRule) throws Exception;

    /**
     * 获取分页结果
     * @param queryRule
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<?> select(QueryRule queryRule, int pageNo, int pageSize) throws Exception;

    /**
     * 根据SQL获取列表
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> selectBySql(String sql, Object... args) throws Exception;

    /**
     * 根据SQL获取分页
     * @param sql
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Map<String, Object>> selectBySqlToPage(String sql, Object [] param, int pageNo, int pageSize) throws Exception;

    /**
     * 删除一条记录
     * @param entity
     * @return
     * @throws Exception
     */
    boolean delete(T entity) throws Exception;

    /**
     * batch delete
     * @param list
     * @return
     * @throws Exception
     */
    int deleteAll(List<T> list) throws Exception;

    /**
     * insert one record and return the ID
     * @param entity
     * @return
     * @throws Exception
     */
    PK insertAndReturnId(T entity) throws Exception;

    /**
     * insert oen record with increment ID
     * @param entity
     * @return
     * @throws Exception
     */
    boolean insert(T entity) throws Exception;

    /**
     * batch insert
     * @param list
     * @return
     * @throws Exception
     */
    int insertAll(List<T> list) throws Exception;

    /**
     * update a record
     * @param entity
     * @return
     * @throws Exception
     */
    boolean update(T entity) throws Exception;
}
