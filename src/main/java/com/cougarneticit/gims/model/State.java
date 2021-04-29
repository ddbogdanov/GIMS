package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Component
@Table(name="states")
public class State {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="state_id")
    private int stateId;
    @Column(name="state_code")
    private String stateCode;
    @Column(name="state_name")
    private String stateName;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="state", cascade = CascadeType.REMOVE)
    private Set<Customer> customers;

    public State() {
        this.stateCode = null;
        this.stateName = null;
    }
    public State(String stateCode, String stateName) {
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    public int getStateId() {
        return stateId;
    }
    public String getStateCode() {
        return stateCode;
    }
    public String getStateName() {
        return stateName;
    }

    @Override
    public String toString() {
        return stateName;
    }
}
