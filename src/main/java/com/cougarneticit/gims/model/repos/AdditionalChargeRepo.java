package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.AdditionalCharge;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdditionalChargeRepo extends CrudRepository<AdditionalCharge, Integer> {
    List<AdditionalCharge> findAll();
}
