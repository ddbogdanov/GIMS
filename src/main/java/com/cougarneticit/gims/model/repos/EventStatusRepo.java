package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.EventStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EventStatusRepo extends CrudRepository<EventStatus, UUID> {
    List<EventStatus> findAll();
}
