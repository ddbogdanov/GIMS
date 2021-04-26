package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.EmployeeReport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeReportRepo extends CrudRepository<EmployeeReport, UUID> {
    List<EmployeeReport> findAll();
}
