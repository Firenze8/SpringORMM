package com.learn.crud;

import com.learn.Page;
import com.learn.QueryRule;

import java.util.List;
import java.util.Map;

public interface SelectTop<T> {
    /**
     * 获取列表
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
     * @throwsException
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
     * @param pageSizhe
     * @return
     * @throws Exception
     */
    Page<Map<String, Object>> selectBySqlToPage(String sql, Object[] param, int pageNo, int pageSizhe) throws Exception;
}
