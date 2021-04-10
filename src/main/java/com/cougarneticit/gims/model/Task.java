package com.cougarneticit.gims.model;

import com.cougarneticit.gims.model.common.Priority;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Component
@Table(name="task")
public class Task {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="task_id", length=36)
    private UUID task_id;
    @Column(name="priority", length=6)
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Column(name="due_date", length=20)
    private String due_date;
    @Column(name="description", length=256)
    private String description;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    public Task() {
        task_id = UUID.randomUUID();
        priority = null;
        due_date = null;
        description = null;
    }
    public Task(UUID task_id, Room room, Employee employee, Priority priority, String due_date, String description) {
        this.task_id = task_id;
        this.room = room;
        this.employee = employee;
        this.priority = priority;
        this.due_date = due_date;
        this.description = description;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
}
