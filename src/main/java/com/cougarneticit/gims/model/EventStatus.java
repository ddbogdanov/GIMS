package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Component
@Table(name="event_status")
public class EventStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="event_status_id")
    private int eventStatusId;
    @Column(name="event_status", length=6)
    private String eventStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="eventStatus", cascade=CascadeType.REMOVE)
    private Set<Event> events;

    public EventStatus() {

    }
    public EventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getEventStatusId() {
        return eventStatusId;
    }
    public String getEventStatus() {
        return eventStatus;
    }

    @Override
    public String toString() {
        return eventStatus;
    }
}
