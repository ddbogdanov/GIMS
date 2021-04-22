package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Stay;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface StayRepo extends CrudRepository<Stay, Integer> {
    List<Stay> findAll();
    List<Stay> findAllByCustomer_CustomerId(UUID customerId);
}
