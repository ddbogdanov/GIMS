package com.cougarneticit.gims.model;

import com.cougarneticit.gims.model.common.RoomStatus;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Entity
@Component
@Table(name="room")
public class Room {
    @Id
    @Column(name="room_id", length=1)
    private Character roomId;
    @Column(name="room_name", length=120)
    private String roomName;
    @Column(name="status", length=8)
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToMany(mappedBy="room")
    private List<Task> tasks;

    @ManyToOne
    private Customer customer;

    public Room() {
        roomId = 0;
        roomName = null;
    }
    public Room(char room_id, String room_name, RoomStatus status) {
        this.roomId = room_id;
        this.roomName = room_name;
        this.status = status;
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
    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String toString() {
        return roomId + ": " + roomName;
    }
}
