package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepo extends CrudRepository<Country, Integer> {
    List<Country> findAll();
    Country findByCountryName(String countryName);
}
