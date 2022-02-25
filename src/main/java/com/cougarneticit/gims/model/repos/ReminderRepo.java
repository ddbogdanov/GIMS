package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Reminder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ReminderRepo extends CrudRepository<Reminder, Integer> {
    List<Reminder> findAll();
    List<Reminder> findAllByUser_UserId(UUID userId);
}
