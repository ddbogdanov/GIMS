package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Component
@Table(name="employee")
public class Employee {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="employee_id", length=36)
    private UUID employee_id;
    @Column(name="employee_name")
    private String employee_name;
    @Column(name="employee_phone")
    private String employee_phone;
    @Column(name="employee_email")
    private String employee_email;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public Employee() {
        employee_id = UUID.randomUUID();
        user = null;
        employee_name = null;
        employee_phone = null;
        employee_email = null;
    }
    public Employee(UUID employee_id, User user, String employee_name, String employee_phone, String employee_email) {
        this.employee_id = employee_id;
        this.user = user;
        this.employee_name = employee_name;
        this.employee_phone = employee_phone;
        this.employee_email = employee_email;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        return this.employee_id.toString() + " " + this.user.getUser_id().toString() + " " + this.employee_name + " " + this.employee_phone + " " + this.employee_email;
    }
}
