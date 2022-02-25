package com.cougarneticit.gims.model.repos;

import com.cougarneticit.gims.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepo extends CrudRepository<Room, Character> {
    List<Room> findAll();
    int countAllByRoomStatus_RoomStatus(String roomStatus);
}
