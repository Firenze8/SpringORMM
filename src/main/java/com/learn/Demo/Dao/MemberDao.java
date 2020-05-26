package com.learn.Demo.Dao;

import com.learn.BaseDaoSupport;
import com.learn.Demo.Member;
import com.learn.QueryRule;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class MemberDao extends BaseDaoSupport<Member, Long> {
    @Override
    protected String getPKColumn() {
        return "id";
    }

    @Resource(name="dataSource")
    public void setDataSource(DataSource dataSource){
        super.setDataSourceReadOnly(dataSource);
        super.setDataSourceWrite(dataSource);
    }

    public List<Member> selectAll() throws Exception {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.andLike("name","Tom%");
        return super.select(queryRule);
    }
}
