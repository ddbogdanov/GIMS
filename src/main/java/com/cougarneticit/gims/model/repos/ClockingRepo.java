package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Clocking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClockingRepo extends CrudRepository<Clocking, Integer> {
    List<Clocking> findAll();
}
