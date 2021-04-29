package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Component
@Table(name="event_charge")
public class EventCharge {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="event_charge_id")
    private int eventChargeId;
    @Column(name="description")
    private String description;
    @Column(name="charge")
    private BigDecimal charge;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    public EventCharge() {

    }
    public EventCharge(Order order, String description, BigDecimal charge) {
        this.order = order;
        this.description = description;
        this.charge = charge;
    }
    public EventCharge(int eventChargeId, Order order, String description, BigDecimal charge) {
        this.eventChargeId = eventChargeId;
        this.order = order;
        this.description = description;
        this.charge = charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public int getChargeId() {
        return eventChargeId;
    }
    public int getOrderId() {
        return order.getOrderId();
    }
    public BigDecimal getCharge() {
        return charge;
    }
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "$" + charge.toString();
    }
}
