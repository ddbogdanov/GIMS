package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Component
@Table(name="clocking")
public class Clocking {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="clocking_id")
    private int clockingId;
    @Column(name="in_datetime")
    private LocalDateTime inDateTime;
    @Column(name="out_datetime")
    private LocalDateTime outDateTime;
    @Column(name="clocked_in")
    private boolean clockedIn;
    @Column(name="clocked_out")
    private boolean clockedOut;
    //TODO boolean clockedIn/clockedOut maybe? remove LocalDateTimes from constructors and use clockIn() and clockOut() maybe?

    @OneToOne(mappedBy="clocking", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
    private Shift shift;

    public Clocking() {
        inDateTime = null;
        outDateTime = null;
        shift = null;
        clockedIn = false;
        clockedOut = false;
    }
    public Clocking(Shift shift) {
        this.shift = shift;
        clockedIn = false;
        clockedOut = false;
    }
    public Clocking(int clockingId, Shift shift, LocalDateTime inDateTime, LocalDateTime outDateTime) {
        this.clockingId = clockingId;
        this.shift = shift;
        this.inDateTime = inDateTime;
        this.outDateTime = outDateTime;
        clockedIn = false;
        clockedOut = false;
    }

    public boolean isClockedIn() {
        return clockedIn;
    }
    public boolean isClockedOut() {
        return clockedOut;
    }

    public void clockIn(LocalDateTime inDateTime) {
        this.inDateTime = inDateTime;
        clockedIn = true;
    }
    public void clockOut(LocalDateTime outDateTime) {
        this.outDateTime = outDateTime;
        clockedOut = true;
    }

    @Override
    public String toString() {
        return "In: " + inDateTime + "Out: " + outDateTime;
    }
}
