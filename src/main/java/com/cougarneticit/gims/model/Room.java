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
    private Character room_id;
    @Column(name="room_name", length=120)
    private String room_name;
    @Column(name="status", length=8)
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToMany(mappedBy="room")
    private List<Task> tasks;

    public Room() {
        room_id = 0;
        room_name = null;
    }
    public Room(char room_id, String room_name, RoomStatus status) {
        this.room_id = room_id;
        this.room_name = room_name;
        this.status = status;
    }

    public void setRoomId(char room_id) {
        this.room_id = room_id;
    }
    public void setRoomName(String room_name) {
        this.room_name = room_name;
    }

    public char getRoomId() {
        return room_id;
    }
    public String getRoomName() {
        return room_name;
    }
    public RoomStatus getStatus() {
        return status;
    }

    public String toString() {
        return room_id + ": " + room_name;
    }
}
