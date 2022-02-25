package com.cougarneticit.gims.model.repos;


import com.cougarneticit.gims.model.EmployeeRate;
import com.cougarneticit.gims.model.RoomRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface RoomRateRepo extends CrudRepository<RoomRate, Integer> {
    List<RoomRate> findAll();
    RoomRate findByRate(BigDecimal rate);
    boolean existsByRate(BigDecimal rate);
    @Transactional
    long deleteByRate(BigDecimal rate);
}
