package com.cougarneticit.gims.model;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@Table(name="location")
public class Location {
    @Id
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name="location_id", length=36)
    private UUID locationId;
    @Column(name="name", length=16)
    private String locationName;
    @Column(name="info", length=255)
    private String locationInfo;
    @Column(name="audienceCapacity")
    private int capacity;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="location", cascade=CascadeType.REMOVE)
    private Set<Event> events;

    public Location() {
        locationId = UUID.randomUUID();
        locationName = null;
        locationInfo = null;
        capacity = 0;
    }
    public Location(UUID locationId, String locationName, String locationInfo, int capacity) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationInfo = locationInfo;
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

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return locationName;
    }
}