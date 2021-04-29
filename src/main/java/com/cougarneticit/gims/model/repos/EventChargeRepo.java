package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.EventCharge;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventChargeRepo extends CrudRepository<EventCharge, Integer> {
    List<EventCharge> findAll();
    List<EventCharge> findAllByOrder_OrderId(int orderId);
}
