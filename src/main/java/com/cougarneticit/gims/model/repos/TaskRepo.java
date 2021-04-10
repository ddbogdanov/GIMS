package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TaskRepo extends CrudRepository<Task, UUID> {

}
