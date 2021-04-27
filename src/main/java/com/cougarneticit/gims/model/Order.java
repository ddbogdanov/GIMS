package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@Table(name="orders")
public class Order {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="order_id", length=36) //VarChar(36) //TODO change to int
    private UUID orderId;
    @Column(name="total")
    private BigDecimal total;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="stay_id")
    private Stay stay;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="order", cascade=CascadeType.REMOVE)
    Set<AdditionalCharge> additionalCharges;

    //TODO relationship with order. Allow adding event charge to order.
    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    public Order() {
        orderId = UUID.randomUUID();
        customer = null;
        total = null;
    }
    public Order(UUID orderId, Customer customer, Stay stay, BigDecimal total) {
        this.orderId = orderId;
        this.customer = customer;
        this.stay = stay;
        this.total = total;
    }

    public UUID getOrderId() {
        return orderId;
    }
    public Customer getCustomer() {
        return customer;
    }
    public Stay getStay() {
        return stay;
    }
    public int getStayId() {
        return stay.getStayId();
    }
    public BigDecimal getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return customer.getCustomerName() + " | $" + total.toString();
    }
}
