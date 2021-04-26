package com.cougarneticit.gims.model.repos;


import com.cougarneticit.gims.model.RoomRate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRateRepo extends CrudRepository<RoomRate, Integer> {
    List<RoomRate> findAll();
}
