package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
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
    @OneToMany(fetch=FetchType.LAZY, mappedBy="room", cascade=CascadeType.REMOVE)
    private Set<Stay> stays;
    @OneToMany(fetch=FetchType.LAZY, mappedBy="room", cascade=CascadeType.REMOVE)
    private Set<RoomReport> roomReports;

    @ManyToOne
    @JoinColumn(name="room_status_id")
    private RoomStatus roomStatus;
    @ManyToOne
    @JoinColumn(name="room_rate_id")
    private RoomRate roomRate;

    public Room() {
        roomId = 0;
        roomName = null;
    }
    public Room(char room_id, String room_name, RoomStatus roomStatus, RoomRate rate) {
        this.roomId = room_id;
        this.roomName = room_name;
        this.roomStatus = roomStatus;
        this.roomRate = rate;
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
        return roomStatus;
    }
    public int getRoomStatusId() {
        return roomStatus.getRoomStatusId();
    }
    public RoomRate getRoomRate() {
        return roomRate;
    }
    public BigDecimal getRate() {
        return roomRate.getRoomRate();
    }
    public Set<Task> getTasks() {
        return tasks;
    }
    public Set<Stay> getStays() {
        return stays;
    }

    public void setRoomStatus(RoomStatus status) {
        this.roomStatus = status;
    }


    public String toString() {
        return roomId + ": " + roomName;
    }
}
