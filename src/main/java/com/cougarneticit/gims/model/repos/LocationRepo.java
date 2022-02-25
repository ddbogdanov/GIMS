package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepo extends JpaRepository<Location, UUID> {
    List<Location> findAll();
}