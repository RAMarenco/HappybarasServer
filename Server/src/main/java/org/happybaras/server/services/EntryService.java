package org.happybaras.server.services;

import org.happybaras.server.domain.dtos.RegisterEntryDTO;
import org.happybaras.server.domain.entities.Entry;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EntryService {
    void registerEntry(User user, House house, User vigilant, RegisterEntryDTO info);
    List<Entry> findAll();
    List<Entry> findByUser(User user);
    List<Entry> findByHouse(House house);
    List<Entry> findByPeriod(LocalDateTime beginDate, LocalDateTime endDate);
    List<Entry> findByUserAndPeriod(User user, LocalDateTime beginDate, LocalDateTime endDate);
    List<Entry> findByHouseAndPeriod(House house, LocalDateTime beginDate, LocalDateTime endDate);

    void removeEntry(Entry entry);
}
