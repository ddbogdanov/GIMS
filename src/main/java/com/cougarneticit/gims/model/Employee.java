package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@Table(name="employee")
public class Employee {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="employee_id", length=36) //VarChar(36)
    private UUID employeeId;
    @Column(name="employee_name", length=50) //VarChar(50)
    private String employeeName;
    @Column(name="employee_phone", length=45) //VarChar(45)
    private String employeePhone;
    @Column(name="employee_email", length=320) //VarChar(320)
    private String employeeEmail;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy="employee", cascade=CascadeType.REMOVE)
    private Set<Task> tasks;
    @OneToMany(fetch = FetchType.EAGER, mappedBy="employee", cascade=CascadeType.REMOVE)
    private Set<Shift> shifts;

    public Employee() {
        employeeId = UUID.randomUUID();
        user = null;
        employeeName = null;
        employeePhone = null;
        employeeEmail = null;
    }
    public Employee(UUID employeeId, User user, String employee_name, String employeePhone, String employeeEmail) {
        this.employeeId = employeeId;
        this.user = user;
        this.employeeName = employee_name;
        this.employeePhone = employeePhone;
        this.employeeEmail = employeeEmail;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }
    public User getUser() {
        return user;
    }
    public UUID getUserId() {
        return user.getUserId();
    }
    public String getName() {
        return employeeName;
    }
    public String getPhone() {
        return employeePhone;
    }
    public String getEmail() {
        return employeeEmail;
    }
    public Set<Task> getTasks() {
        return tasks;
    }
    public Set<Shift> getShifts() {
        return shifts;
    }

    public String toString() {
        return this.employeeName;
    }
    public String toStringFull() {
        return this.employeeId.toString() + " " + this.user.getUserId().toString() + " " + this.employeeName + " " + this.employeePhone + " " + this.employeeEmail;
    }
}
