package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Component
@Table(name="additional_charge")
public class AdditionalCharge {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="charge_id")
    private int chargeId;
    @Column(name="description")
    private String description;
    @Column(name="charge")
    private BigDecimal charge;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    public AdditionalCharge() {

    }
    public AdditionalCharge(Order order, String description, BigDecimal charge) {
        this.order = order;
        this.description = description;
        this.charge = charge;
    }
    public AdditionalCharge(int chargeId, Order order, String description, BigDecimal charge) {
        this.chargeId = chargeId;
        this.order = order;
        this.description = description;
        this.charge = charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public int getChargeId() {
        return chargeId;
    }
    public Order getOrder() {
        return order;
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
