package com.learn;

import java.util.List;

public interface CrudTop<T> {
    /**
     * ]获取列表
     * @param queryRule
     * @return
     * @throws Exception
     */
    List<T> select(QueryRule queryRule) throws Exception;


}
