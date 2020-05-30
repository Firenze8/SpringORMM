package com.orm.test;

import com.annotation.Transactional;
import com.learn.Demo.Dao.MemberDao;
import com.learn.Demo.Dao.OrderDao;
import com.learn.Demo.Member;
import com.learn.Demo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AnnotationOrderService implements OrderService{
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmdd");

    @Transactional
    public void transfer2(MemberDao memberDao, OrderDao orderDao) {
        try {
            List<Member> memberList = memberDao.selectAll();
            System.out.println(Arrays.toString(memberList.toArray()));
            for(int age = 25; age < 26; age++){
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

    @Transactional
    @Override
    public void transfer(MemberDao memberDao, OrderDao orderDao) {
        try {
            List<Member> memberList = memberDao.selectAll();
            System.out.println(Arrays.toString(memberList.toArray()));
            Order order = new Order();
            order.setMemberId(1L);
            order.setDetail("历史订单");
            Date date = sdf.parse("20180201123456");
            order.setCreateTime(date.getTime());
            orderDao.insertOne(order);
            for(int age = 25; age < 26; age++){
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
}
