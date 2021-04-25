package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Component
@Table(name="event")
public class Event {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="event_id", length=36)
    private final UUID eventId = UUID.randomUUID();
    @Column(name="name", length=16)
    private String eventName;
    @Column(name="info", length=255)
    private String eventInfo;
    @Column(name="start_date")
    private Date startDate;
    @Column(name="end_date")
    private Date endDate;

    @OneToOne
    @JoinColumn(name="location_id")
    private Location location;

    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "event", orphanRemoval = false)
    //private Set<Customer> customers;

    @ManyToOne
    @JoinColumn(name="event_status_id")
    EventStatus eventStatus;

    public Event() {

    }
    public Event(String eventName, String eventInfo, Date startDate, Date endDate, Location location) {
        this.eventName = eventName;
        this.eventInfo = eventInfo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }

    public UUID getEventId() {
        return eventId;
    }
    public String getEventName() {
        return eventName;
    }
    public String getEventInfo() {
        return eventInfo;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public Location getLocation() {
        return location;
    }
    public int getEventStatusId() {
        return eventStatus.getEventStatusId();
    }
    public EventStatus getEventStatus() {
        return eventStatus;
    }

    /*public Set<Customer> getCustomers() {
        return customers;
    }*/



    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public void setStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    /*public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }*/

    @Override
    public String toString() {
        return eventName;
    }
}