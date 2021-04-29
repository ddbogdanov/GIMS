package com.cougarneticit.gims.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Component
@Table(name="country")
public class Country {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="country_id")
    private int countryId;
    @Column(name="country_name")
    private String countryName;

    @OneToMany(fetch=FetchType.EAGER, mappedBy="country", cascade = CascadeType.REMOVE)
    private Set<Customer> customers;

    public Country() {

    }
    public Country(String countryName) {
        this.countryName = countryName;
    }

    public int getCountryId() {
        return countryId;
    }
    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
