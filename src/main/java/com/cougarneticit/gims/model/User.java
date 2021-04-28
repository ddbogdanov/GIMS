package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@Table(name="users")
public class User {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="user_id", length=36) //varchar(36)
    private UUID userId;
    @Column(name="username", length=16) //varchar(16)
    private String username;
    @Column(name="password", length=140) //varchar(140)
    private String password;
    @Column(name="isadmin") //tinyint(1)
    private boolean isadmin;

    @OneToOne(mappedBy="user", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
    private Employee employee;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="user", cascade=CascadeType.REMOVE)
    Set<Reminder> reminders;

    public User() {
        userId = UUID.randomUUID();
        this.username = null;
        this.password = null;
        this.isadmin = false;
    }
    public User(UUID id, String username, String password, boolean isadmin) {
        this.userId = id;
        this.username = username;
        this.password = password;
        this.isadmin = isadmin;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public UUID getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Employee getEmployee() {
        return employee;
    }
    public UUID getEmployeeId() {
        return employee.getEmployeeId();
    }
    public boolean isEmployee() {
        return !(employee == null);
    }
    public boolean isAdmin() { return isadmin; }

    public String toString() {
        return this.username;
    }
    public String toStringFull() {
        return this.userId.toString() + " " + this.username + " " + this.password + " " + this.isadmin;
    }
}