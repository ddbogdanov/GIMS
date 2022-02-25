package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Component
@Table(name="employee_report")
public class EmployeeReport {

    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="employee_report_id", length=36) //VarChar(36)
    private UUID employeeReportId;
    @Column(name="date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    public EmployeeReport() {
        employeeReportId = UUID.randomUUID();
        employee = null;
        date = null;
    }
    public EmployeeReport(UUID employeeReportId, Employee employee, LocalDate date) {
        this.employeeReportId = employeeReportId;
        this.employee = employee;
        this.date = date;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Employee Report | Requested on: | " + date;
    }
}
