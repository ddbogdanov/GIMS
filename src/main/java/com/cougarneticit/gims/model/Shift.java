package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Component
@Table(name="shift")
public class Shift {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="shift_id", length=36) //VarChar(36)
    private UUID shiftId;
    @Column(name="start_datetime", length=28) //VarChar(28)
    private String startDateTime;
    @Column(name="end_datetime", length=28)
    private String endDateTime;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    public Shift() {
        this.shiftId = UUID.randomUUID();
        this.startDateTime = null;
        this.endDateTime = null;
        this.employee = null;
    }
    public Shift(UUID shiftId, Employee employee, String startDateTime, String endDateTime) {
        this.shiftId = shiftId;
        this.employee = employee;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void setShiftId(UUID shiftId) {
        this.shiftId = shiftId;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }
    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public UUID getShiftId() {
        return shiftId;
    }
    public String getStartDateTime() {
        return startDateTime;
    }
    public String getEndDateTime() {
        return endDateTime;
    }
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return employee.getName() + " | " + startDateTime + " -> " + endDateTime;
    }
}
