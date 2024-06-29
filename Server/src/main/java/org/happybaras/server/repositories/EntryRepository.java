package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.Entry;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EntryRepository extends JpaRepository<Entry, UUID> {
    List<Entry> findByUser(User user);
    List<Entry> findByHouse(House house);
    List<Entry> findByTimestampBetween(LocalDateTime beginDate, LocalDateTime endDate);
    List<Entry> findByUserAndTimestampBetween(User user, LocalDateTime beginDate, LocalDateTime endDate);
    List<Entry> findByHouseAndTimestampBetween(House house, LocalDateTime beginDate, LocalDateTime endLocalDateTime);
}
