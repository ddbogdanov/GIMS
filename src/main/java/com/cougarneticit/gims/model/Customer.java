package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
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
    @Column(name="extra_info", length=320) //VarChar(320)
    private String extraInfo;
    @Column(name="start_date") //VarChar(28)
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    //TODO: One to One
    @Column(name = "room_id")
    private char roomId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="event_id")
    private Event event;

    public String getExtraInfo() {
        return extraInfo;
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
    public char getRoomId() {
        return roomId;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
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
    public void setRoom(char roomId) {
        this.roomId = roomId;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Customer() {

    }
    public Customer(UUID customerId, String customerName, String customerPhone, String customerEmail, String extraInfo, Date startDate, Date endDate) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.extraInfo = extraInfo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return customerName;
    }
}
