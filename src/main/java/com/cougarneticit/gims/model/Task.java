package com.cougarneticit.gims.model;

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
    private UUID taskId;
    @Column(name="name")
    private String name;
    @Column(name="due_date", length=20)
    private String dueDate;
    @Column(name="description", length=256)
    private String description;
    @Column(name="completed", length=1)
    private boolean completed;

    @ManyToOne
    @JoinColumn(name="priority_id")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;

    public Task() {
        taskId = UUID.randomUUID();
        priority = null;
        dueDate = null;
        description = null;
    }
    public Task(UUID taskId, Room room, Employee employee, String name, Priority priority, String dueDate, String description) {
        this.taskId = taskId;
        this.room = room;
        this.employee = employee;
        this.name = name;
        this.priority = priority;
        this.dueDate = dueDate;
        this.description = description;
    }

    public UUID getTaskId() {
        return taskId;
    }
    public Room getRoom() {
        return room;
    }
    public Employee getEmployee() {
        return employee;
    }
    public String getName() {
        return name;
    }
    public Priority getPriority() {
        return priority;
    }
    public int getPriorityId() {
        return priority.getPriorityId();
    }
    public String getDueDate() {
        return dueDate;
    }
    public String getDescription() {
        return description;
    }
    public boolean isCompleted() {
        return completed;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        String c = completed ? "Yes" : "No ";
        return "Priority: " + String.format("%1$-6s", priority.toString()) + " | Completed?: " + c + " | " + name;
    }
}
