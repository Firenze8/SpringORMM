package com.orm.test;
import com.learn.Demo.Dao.MemberDao;
import com.learn.Demo.Dao.OrderDao;
import com.learn.Demo.Member;
import com.learn.Demo.Order;
import com.transaction.TransactionEnabledProxyManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ContextConfiguration(locations = {"classpath:application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class OrmTest {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmdd");

    @Autowired private MemberDao memberDao;

    @Autowired private OrderDao orderDao;

    //约定优于配置
    //先制定顶层接口，参数返回值全部统一
    //List<?> Page<?> select(QueryRule queryRule)
    //Int delete(T entity)
    //ReturnId insert(T entity)
    //Int update(T entity)
    @Test
    public void testSelectAllForMember(){
        try {
            List<Member> result = memberDao.selectAll();
            System.out.println(Arrays.toString(result.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    //@Ignore
    public void testInsertMember(){
        try {
            for(int age = 25; age < 35; age++){
                Member member = new Member();
                member.setAge(age);
                member.setName("Tom");
                member.setAddr("Hunan Changsha");
                memberDao.insert(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertOrder(){
        try {
            Order order = new Order();
            order.setMemberId(1L);
            order.setDetail("历史订单");
            Date date = sdf.parse("20180201123456");
            order.setCreateTime(date.getTime());
            orderDao.insertOne(order);
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Resource(name="dataSource")
    private DataSource dataSource;
    @Test
    public void transfer(){
        TransactionEnabledProxyManager transactionEnabledProxyManager = new TransactionEnabledProxyManager(new DataSourceTransactionManager(dataSource));
        OrderService orderService = new AnnotationOrderService();
        OrderService proxyOderService = (OrderService)transactionEnabledProxyManager.proxyFor(orderService);
        proxyOderService.transfer(memberDao,orderDao);
    }

    public int testFinal(){
        try {
            int i  = 1 / 0;
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("我会不会不抛出？");
        } finally {
            return 3;
        }
    }

}
