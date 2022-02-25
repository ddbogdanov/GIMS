package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@Table(name="customer")
public class Customer {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="customer_id", length=36) //VarChar(36)
    private UUID customerId;
    @Column(name="customer_name", length=50) //VarChar(50)
    private String customerName;
    @Column(name="customer_phone", length=45) //VarChar(45)
    private String customerPhone;
    @Column(name="customer_email", length=320) //VarChar(320)
    private String customerEmail;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="customer", cascade=CascadeType.REMOVE, orphanRemoval=true)
    private Set<Stay> stays;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="customer", cascade=CascadeType.REMOVE)
    private Set<Order> orders;

    @ManyToOne
    @JoinColumn(name="state_id")
    private State state;
    @ManyToOne
    @JoinColumn(name="country_id")
    private Country country;

    public Customer() {

    }
    public Customer(UUID customerId, String customerName, String customerPhone, String customerEmail, Country country, State state) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.country = country;
        this.state = state;
    }

    public UUID getCustomerId() {
        return customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerPhone() {
        return customerPhone;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public String getCountryString() {
        return country.toString();
    }
    public String getStateString() {
        return state.toString();
    }
    public Set<Stay> getStays() {
        return stays;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString() {
        return customerName;
    }
}
