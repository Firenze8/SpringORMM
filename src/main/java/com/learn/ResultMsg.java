package com.learn;

import java.io.Serializable;

public class ResultMsg<T> implements Serializable {

    private static final long serialVersionUID = -7734118889216283577L;

    private int status;//status code

    private String msg; //explain for status code

    private T data; //put result

    public ResultMsg(){}

    public ResultMsg(int status){
        this.status = status;
    }

    public ResultMsg(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public ResultMsg(int status, T data){
        this.status = status;
        this.data = data;
    }

    public ResultMsg(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
