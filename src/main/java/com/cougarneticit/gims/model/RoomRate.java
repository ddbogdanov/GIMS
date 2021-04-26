package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Component
@Table(name="room_rate")
public class RoomRate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="room_rate_id")
    private int roomRateId;
    @Column(name="rate")
    private BigDecimal rate;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="roomRate", cascade=CascadeType.REMOVE)
    private Set<Room> rooms;

    public RoomRate() {
        rate = null;
    }
    public RoomRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setRoomRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getRoomRate() {
        return rate;
    }
}
