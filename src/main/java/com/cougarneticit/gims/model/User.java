package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Component
@Table(name="user")
public class User {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="user_id", length=36) //varchar(36)
    private UUID user_id;
    @Column(name="username", length=16) //varchar(16)
    private String username;
    @Column(name="password", length=140) //varchar(140)
    private String password;
    @Column(name="isadmin") //tinyint(1)
    private boolean isadmin;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinTable(name="employee",
            joinColumns =
                    { @JoinColumn(name="user_id", referencedColumnName = "user_id")},
            inverseJoinColumns =
                    { @JoinColumn(name="employee_id", referencedColumnName = "employee_id")})
    private Employee employee;

    public User() {
        user_id = UUID.randomUUID();
        this.username = null;
        this.password = null;
        this.isadmin = false;
    }
    public User(UUID id, String username, String password, boolean isadmin) {
        this.user_id = id;
        this.username = username;
        this.password = password;
        this.isadmin = isadmin;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public UUID getUser_id() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public boolean isAdmin() { return isadmin; }

    public String toString() {
        return this.username;
    }
    public String toStringFull() {
        return this.user_id.toString() + " " + this.username + " " + this.password + " " + this.isadmin;
    }
}