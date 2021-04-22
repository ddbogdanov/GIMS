package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepo extends CrudRepository<Task, UUID> {
    List<Task> findAll();
    List<Task> findAllByRoom_RoomId(Character roomId);
    List<Task> findAllByEmployee_EmployeeId(UUID employeeId);
    int countAllByEmployee_EmployeeIdAndCompleted(UUID employeeId, boolean completed);
    int countAllByRoom_RoomId(Character roomId);
}
