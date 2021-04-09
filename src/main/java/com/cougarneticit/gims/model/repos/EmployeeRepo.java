package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepo extends CrudRepository<Employee, UUID> {
    List<Employee> findAll();
}
