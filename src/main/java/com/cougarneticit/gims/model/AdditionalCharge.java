package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
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
    private BigInteger charge;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    public AdditionalCharge() {

    }
    public AdditionalCharge(Order order, String description, BigInteger charge) {
        this.order = order;
        this.description = description;
        this.charge = charge;
    }

    @Override
    public String toString() {
        return charge.toString();
    }
}
