package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.RoomReport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface RoomReportRepo extends CrudRepository<RoomReport, UUID> {
    List<RoomReport> findAll();
}
