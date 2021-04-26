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
    @Column(name="order_id", length=36) //VarChar(36)
    private UUID orderId;
    @Column(name="total")
    private BigDecimal total;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="order", cascade=CascadeType.REMOVE)
    Set<AdditionalCharge> additionalCharges;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    public Order() {
        orderId = UUID.randomUUID();
        customer = null;
        total = null;
    }
    public Order(UUID orderId, Customer customer, BigDecimal total) {
        this.orderId = orderId;
        this.customer = customer;
        this.total = total;
    }

    @Override
    public String toString() {
        return customer.getCustomerName() + " | " + total.toString();
    }
}
