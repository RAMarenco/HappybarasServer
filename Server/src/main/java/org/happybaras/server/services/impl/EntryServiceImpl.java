package org.happybaras.server.services.impl;

import jakarta.transaction.Transactional;
import org.happybaras.server.domain.dtos.RegisterEntryDTO;
import org.happybaras.server.domain.entities.Entry;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.repositories.EntryRepository;
import org.happybaras.server.repositories.UserRepository;
import org.happybaras.server.services.EntryService;
import org.happybaras.server.services.HouseService;
import org.happybaras.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EntryServiceImpl implements EntryService {
    private final EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void registerEntry(User user, House house, User vigilant, RegisterEntryDTO info) {
        Entry entry = new Entry();

        entry.setUser(user);
        entry.setHouse(house);
//        entry.setVigilant(vigilant);
        entry.setComment(info.getDescription());
        entry.setDocument(info.getIdentityDocument());
        entry.setTimestamp(LocalDateTime.now());

        entryRepository.save(entry);
    }

    @Override
    public List<Entry> findAll() {
        return entryRepository.findAll();
    }

    @Override
    public List<Entry> findByUser(User user) {
        return entryRepository.findByUser(user);
    }

    @Override
    public List<Entry> findByHouse(House house) {
        return entryRepository.findByHouse(house);
    }

    @Override
    public List<Entry> findByPeriod(LocalDateTime beginDate, LocalDateTime endDate) {
        return entryRepository.findByTimestampBetween(beginDate, endDate);
    }

    @Override
    public List<Entry> findByUserAndPeriod(User user, LocalDateTime beginDate, LocalDateTime endDate) {
        return entryRepository.findByUserAndTimestampBetween(user, beginDate, endDate);
    }

    @Override
    public List<Entry> findByHouseAndPeriod(House house, LocalDateTime beginDate, LocalDateTime endDate) {
        return entryRepository.findByHouseAndTimestampBetween(house, beginDate, endDate);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void removeEntry(Entry entry) {
        entry.setHouse(null);
        entryRepository.delete(entry);
    }
}
