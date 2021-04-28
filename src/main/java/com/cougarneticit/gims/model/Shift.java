package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
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
    private LocalDateTime startDateTime;
    @Column(name="end_datetime", length=28)
    private LocalDateTime endDateTime;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="clocking_id")
    private Clocking clocking;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    public Shift() {
        this.shiftId = UUID.randomUUID();
        this.startDateTime = null;
        this.endDateTime = null;
        this.employee = null;
        clocking = new Clocking(this);
    }
    public Shift(UUID shiftId, Employee employee, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.shiftId = shiftId;
        this.employee = employee;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        clocking = new Clocking(this);
    }

    public void setShiftId(UUID shiftId) {
        this.shiftId = shiftId;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public UUID getShiftId() {
        return shiftId;
    }
    public UUID getEmployeeId() {
        return employee.getEmployeeId();
    }
    public Employee getEmployee() {
        return employee;
    }
    public Clocking getClocking() {
        return clocking;
    }
    public boolean isClockedIn() {
        return clocking.isClockedIn();
    }
    public boolean isClockedOut() {
        return clocking.isClockedOut();
    }
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    @Override
    public String toString() {
        return
                startDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC")))
                + " To " +
                endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US).withZone(ZoneId.of("Etc/UTC")));
    }
}
