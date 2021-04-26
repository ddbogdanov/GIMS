package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Component
@Table(name="location")
public class Location {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="location_id", length=36)
    private final UUID locationId = UUID.randomUUID();;
    @Column(name="name", length=16)
    private String locationName;
    @Column(name="info", length=255)
    private String locationInfo;
    @Column(name="audienceCapacity")
    private int capacity;

    public Location() {

    }
    public Location(String locationName, String locationInfo, int capacity) {
        this.locationName = locationName;
        this.locationInfo = locationInfo;
        this.capacity = capacity;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public UUID getLocationId() {
        return locationId;
    }
    public String getLocationName() {
        return locationName;
    }
    public String getLocationInfo() {
        return locationInfo;
    }
    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return locationName;
    }
}