package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepo extends CrudRepository<Task, UUID> {
    List<Task> findAll();
    List<Task> findAllByRoom_RoomId(Character roomId);
    List<Task> findAllByEmployee_EmployeeId(UUID employeeId);
    List<Task> findAllByEmployee_EmployeeIdAndRoom_RoomId(UUID employeeId, Character roomId);
    int countAllByEmployee_EmployeeIdAndCompleted(UUID employeeId, boolean completed);
    int countAllByRoom_RoomId(Character roomId);
    int countAllByRoom_RoomIdAndCompleted(Character roomId, boolean completed);
    int countAllByCompleted(boolean completed);
}
