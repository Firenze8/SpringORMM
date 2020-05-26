package com.learn.Demo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="t_order")
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 3981796745099268775L;
    private Long id;
    @Column(name="mid")
    private Long memberId;
    private String detail;
    private Long createTime;
    private String createTimeFmt;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", detail='" + detail + '\'' +
                ", createTime=" + createTime +
                ", createTimeFmt='" + createTimeFmt + '\'' +
                '}';
    }
}
