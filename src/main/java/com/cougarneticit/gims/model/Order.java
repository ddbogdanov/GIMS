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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="order_id")
    private int orderId;
    @Column(name="total")
    private BigDecimal total;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="stay_id")
    private Stay stay;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="order", cascade=CascadeType.REMOVE)
    Set<AdditionalCharge> additionalCharges;
    @OneToMany(fetch=FetchType.EAGER, mappedBy="order", cascade=CascadeType.REMOVE)
    Set<EventCharge> eventCharges;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    public Order() {
        customer = null;
        stay = null;
        total = null;
    }
    public Order(Customer customer, Stay stay, BigDecimal total) {
        this.customer = customer;
        this.stay = stay;
        this.total = total;
    }
    public Order(int orderId, Customer customer, Stay stay, BigDecimal total) {
        this.orderId = orderId;
        this.customer = customer;
        this.stay = stay;
        this.total = total;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getOrderId() {
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
    public Set<AdditionalCharge> getAdditionalCharges() {
        return additionalCharges;
    }
    public Set<EventCharge> getEventCharges() {
        return eventCharges;
    }

    public void addToTotal(BigDecimal total) {
        this.total = this.total.add(total);
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " | $" + total.toString();
    }
}
