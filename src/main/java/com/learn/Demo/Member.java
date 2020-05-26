package com.learn.Demo;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="t_member")
@Data
public class Member implements Serializable {
    private static final long serialVersionUID = 5046056529518678175L;
    @Id private Long id;
    private String name;
    private String addr;
    private Integer age;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", age=" + age +
                '}';
    }
}
