package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.RoomStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomStatusRepo extends CrudRepository<RoomStatus, Integer> {
    List<RoomStatus> findAll();
}
