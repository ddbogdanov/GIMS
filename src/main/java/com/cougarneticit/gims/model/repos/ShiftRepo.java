package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Shift;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ShiftRepo extends CrudRepository<Shift, UUID> {
    List<Shift> findAllByEmployee_EmployeeId(UUID employeeId);
}
