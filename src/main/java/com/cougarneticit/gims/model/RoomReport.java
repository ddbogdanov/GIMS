package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Component
@Table(name="room_report")
public class RoomReport {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="room_report_id", length=36) //VarChar(36)
    private UUID roomReportId;
    @Column(name="date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    public RoomReport() {
        roomReportId = UUID.randomUUID();
        room = null;
        date = null;
    }
    public RoomReport(UUID roomReportId, Room room, LocalDate date) {
        this.roomReportId = roomReportId;
        this.room = room;
        this.date = date;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Room Report | Requested on: | " + date;
    }
}
