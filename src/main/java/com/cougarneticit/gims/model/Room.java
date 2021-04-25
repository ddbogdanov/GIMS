package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Component
@Table(name="room")
public class Room {
    @Id
    @Column(name="room_id", length=1)
    private Character roomId;
    @Column(name="room_name", length=120)
    private String roomName;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="room", cascade=CascadeType.REMOVE)
    private Set<Task> tasks;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="room", cascade=CascadeType.REMOVE)
    private Set<Stay> stays;

    @ManyToOne
    @JoinColumn(name="room_status_id")
    private RoomStatus eventStatus;

    public Room() {
        roomId = 0;
        roomName = null;
    }
    public Room(char room_id, String room_name, RoomStatus eventStatus) {
        this.roomId = room_id;
        this.roomName = room_name;
        this.eventStatus = eventStatus;
    }

    public void setRoomId(char room_id) {
        this.roomId = room_id;
    }
    public void setRoomName(String room_name) {
        this.roomName = room_name;
    }

    public char getRoomId() {
        return roomId;
    }
    public String getRoomName() {
        return roomName;
    }
    public RoomStatus getRoomStatus() {
        return eventStatus;
    }
    public int getRoomStatusId() {
        return eventStatus.getRoomStatusId();
    }
    public Set<Task> getTasks() {
        return tasks;
    }
    public Set<Stay> getStays() {
        return stays;
    }

    public void setEventStatus(RoomStatus status) {
        this.eventStatus = status;
    }


    public String toString() {
        return roomId + ": " + roomName;
    }
}
