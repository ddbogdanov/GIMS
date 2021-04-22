package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Component
@Table(name="stay")
public class Stay {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="stay_id")
    private int stayId;
    @Column(name="start_date")
    private LocalDate startDate;
    @Column(name="end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    public Stay() {
        startDate = null;
        endDate = null;
    }
    public Stay(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return startDate + " " + endDate;
    }
}
