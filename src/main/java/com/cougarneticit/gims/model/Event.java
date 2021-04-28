package com.cougarneticit.gims.model;

//import com.cougarneticit.gims.model.common.EventStatus;
import java.time.LocalDate;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Component
@Table(name="event")
public class Event {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="event_id", length=36)
    private UUID eventId;
    @Column(name="name", length=16)
    private String eventName;
    @Column(name="info", length=255)
    private String eventInfo;
    @Column(name="start_date")
    private LocalDateTime startDateTime;
    @Column(name="end_date")
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;
    @ManyToOne
    @JoinColumn(name="event_status_id")
    private EventStatus eventStatus;

    public Event() {

    }
    public Event(UUID eventId, Location location, EventStatus eventStatus, String eventName, String eventInfo, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.eventId = eventId;
        this.location = location;
        this.eventStatus = eventStatus;
        this.eventName = eventName;
        this.eventInfo = eventInfo;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }
    public Location getLocation() {
        return location;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return eventName;
    }
}