package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Component
@Table(name="employee_rate")
public class EmployeeRate {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="employee_rate_id")
    private int employeeRateId;
    @Column(name="rate")
    private BigDecimal rate;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="employeeRate", cascade = CascadeType.REMOVE)
    private Set<Employee> employees;

    public EmployeeRate() {
        rate = null;
    }
    public EmployeeRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getEmployeeStringRate() {
        return rate.toString();
    }

    @Override
    public String toString() {
        return rate.toString();
    }
}
