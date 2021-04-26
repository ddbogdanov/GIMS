package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.EmployeeRate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRateRepo extends CrudRepository<EmployeeRate, Integer > {
    List<EmployeeRate> findAll();
}
