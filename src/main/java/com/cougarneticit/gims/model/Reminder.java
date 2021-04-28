package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Component
@Table(name="reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="reminder_id")
    private int reminderId;
    @Column(name="reminder")
    private String reminder;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Reminder() {
        reminder = null;
    }
    public Reminder(User user, String reminder) {
        this.user = user;
        this.reminder = reminder;
    }

    @Override
    public String toString() {
        return reminder;
    }
}
