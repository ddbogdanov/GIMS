package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Priority;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriorityRepo extends CrudRepository<Priority, Integer> {
    List<Priority> findAll();
}
