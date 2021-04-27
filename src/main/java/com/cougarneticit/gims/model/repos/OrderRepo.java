package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepo extends CrudRepository<Order, UUID> {
    List<Order> findAll();
    List<Order> findAllByCustomer_CustomerId(UUID customerId);
}
