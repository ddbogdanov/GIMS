package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Component
@Table(name="priority")
public class Priority {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="priority_id")
    private int priorityId;
    @Column(name="priority", length=6)
    private String priority;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="priority", cascade=CascadeType.REMOVE)
    private Set<Task> tasks;

    public Priority() {

    }
    public Priority(String priority) {
        this.priority = priority;
    }

    public int getPriorityId() {
        return priorityId;
    }
    public String getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return priority;
    }
}
