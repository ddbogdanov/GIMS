package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Reminder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReminderRepo extends CrudRepository<Reminder, Integer> {
    List<Reminder> findAll();
}
