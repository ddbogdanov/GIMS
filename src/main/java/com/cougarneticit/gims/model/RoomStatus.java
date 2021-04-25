package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Component
@Table(name="room_status")
public class RoomStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="room_status_id")
    private int roomStatusId;
    @Column(name="status", length=8)
    private String eventStatus;

    @OneToMany(fetch = FetchType.EAGER, mappedBy= "eventStatus", cascade=CascadeType.REMOVE)
    private Set<Room> rooms;

    public RoomStatus() {

    }
    public RoomStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public int getRoomStatusId() {
        return roomStatusId;
    }
    public String getEventStatus() {
        return eventStatus;
    }

    @Override
    public String toString() {
        return eventStatus;
    }
}
