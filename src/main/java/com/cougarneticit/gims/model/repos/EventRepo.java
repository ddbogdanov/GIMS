package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepo extends JpaRepository<Event, UUID> {
    List<Event> findAll();
}