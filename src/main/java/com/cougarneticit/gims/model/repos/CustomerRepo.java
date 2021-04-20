package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepo extends CrudRepository<Customer, UUID> {
    List<Customer> findAll();
}
