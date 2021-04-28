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
    int clockingId;
    @Column(name="in_datetime")
    LocalDateTime inDateTime;
    @Column(name="out_datetime")
    LocalDateTime outDateTime;

    //TODO boolean clockedIn/clockedOut maybe? remove LocalDateTimes from constructors and use clockIn() and clockOut() maybe?

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="shift_id")
    private Shift shift;

    public Clocking() {
        inDateTime = null;
        outDateTime = null;
        shift = null;
    }
    public Clocking(Shift shift, LocalDateTime inDateTime, LocalDateTime outDateTime) {
        this.shift = shift;
        this.inDateTime = inDateTime;
        this.outDateTime = outDateTime;
    }
    public Clocking(int clockingId, Shift shift, LocalDateTime inDateTime, LocalDateTime outDateTime) {
        this.clockingId = clockingId;
        this.shift = shift;
        this.inDateTime = inDateTime;
        this.outDateTime = outDateTime;
    }

    @Override
    public String toString() {
        return "In: " + inDateTime + "Out: " + outDateTime;
    }
}
