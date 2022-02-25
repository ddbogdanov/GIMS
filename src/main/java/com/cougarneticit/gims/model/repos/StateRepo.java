package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepo extends CrudRepository<State, Integer> {
    List<State> findAll();
    State findByStateName(String stateName);
}
