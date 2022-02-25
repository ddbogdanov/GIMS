package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.UUID;

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

    @OneToOne(mappedBy="stay", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    private Order order;

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
    public Stay(Customer customer, Room room, LocalDate startDate, LocalDate endDate) {
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Stay(int stayId, Customer customer, Room room, LocalDate startDate, LocalDate endDate) {
        this.stayId = stayId;
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getStayId() {
        return stayId;
    }
    public UUID getCustomerId() {
        return customer.getCustomerId();
    }
    public String getCustomerName() {
        return customer.getCustomerName();
    }
    public Room getRoom() {
        return room;
    }
    public char getRoomId() {
        return room.getRoomId();
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return
                "Suite " + room.getRoomId() + " | " +
                        startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC"))) +
                        " to " +
                        endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC")));
    }
}
