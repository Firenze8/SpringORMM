package com.orm.test;

import com.learn.Demo.Dao.MemberDao;
import com.learn.Demo.Dao.OrderDao;

public interface OrderService {
    public void transfer(MemberDao memberDao, OrderDao orderDao);
}
