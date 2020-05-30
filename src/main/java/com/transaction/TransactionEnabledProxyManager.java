package com.transaction;

import com.annotation.Transactional;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionEnabledProxyManager {
    private DataSourceTransactionManager transactionManager;

    public TransactionEnabledProxyManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Object proxyFor(Object object){
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), new TransactionInvocationHandler(object, transactionManager));
    }
}

class TransactionInvocationHandler implements InvocationHandler{
    private Object proxied;
    private DataSourceTransactionManager transactionManager;

    public TransactionInvocationHandler(Object object, DataSourceTransactionManager transactionManager) {
        this.proxied = object;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        Method originalMethod = proxied.getClass().getMethod(method.getName(), method.getParameterTypes());
        if(!originalMethod.isAnnotationPresent(Transactional.class)){
            return method.invoke(proxied, objects);
        }
        System.out.println(proxy.getClass().getSimpleName());
        // start transaction
        TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionAttribute());
        Object result = null;
        try {
            result = method.invoke(proxied, objects);
            transactionManager.commit(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(transaction);
        } finally {
        }
        return result;
    }
}
