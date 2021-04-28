package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepo extends JpaRepository<Event, UUID> {
    List<Event> findAll();
}
