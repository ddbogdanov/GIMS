package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.EmployeeRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRateRepo extends CrudRepository<EmployeeRate, Integer > {
    List<EmployeeRate> findAll();
    EmployeeRate findByRate(BigDecimal rate);
    boolean existsByRate(BigDecimal rate);
    @Transactional
    long deleteByRate(BigDecimal rate);
}
