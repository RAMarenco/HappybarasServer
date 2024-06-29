package org.happybaras.server.services.impl;

import jakarta.transaction.Transactional;
import org.happybaras.server.domain.dtos.AddressDTO;
import org.happybaras.server.domain.dtos.PagedHouseDTO;
import org.happybaras.server.domain.dtos.RegisterHouseDTO;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.repositories.HouseRepository;
import org.happybaras.server.repositories.UserRepository;
import org.happybaras.server.services.EntryService;
import org.happybaras.server.services.HouseService;
import org.happybaras.server.services.PermitService;
import org.happybaras.server.services.UserService;
import org.happybaras.server.utils.DTOConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;
    private final UserService userService;
    private final PermitService permitService;
    private final EntryService entryService;
    private final DTOConverters dtoConverters;

    public HouseServiceImpl(HouseRepository houseRepository, UserService userService, PermitService permitService, EntryService entryService, DTOConverters dtoConverters) {
        this.houseRepository = houseRepository;
        this.userService = userService;
        this.permitService = permitService;
        this.entryService = entryService;
        this.dtoConverters = dtoConverters;
    }

    @Override
    public List<House> findAll() {
        return houseRepository.findAll();
    }

    @Override
    public PagedHouseDTO findHouses(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return dtoConverters.convertToPagedHouseDTO(houseRepository.findAll(pageable));
    }

    @Override
    public int getTotalPages(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<House> housePage = houseRepository.findAll(pageable);
        return housePage.getTotalPages();
    }

    @Override
    public House findByHouseNumber(String number) {
        return houseRepository.findByHouseNumberEquals(number).orElse(null);
    }

    @Override
    public House findByOwner(User user) {
        return houseRepository.findByOwner(user).orElse(null);
    }

    @Override
    public House findById(UUID id) {
        return houseRepository.findById(id).orElse(null);
    }

    @Override
    public List<AddressDTO> findAllAddresses() {
        return houseRepository.findAll().stream()
                .map(dtoConverters::convertToAddressDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(RegisterHouseDTO info, User owner) {
        House house = new House();

        house.setHouseNumber(info.getHouseNumber());
        house.setAddress(info.getAddress());
        house.setTelephone(info.getTelephone());
        house.setOwner(owner);
        house.setNumberOfHabitants(0);

        houseRepository.save(house);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(House house) {
        house.getUsers().forEach(userService::removeHouse);
        house.getPermits().forEach(permitService::removePermit);
        house.getEntries().forEach(entryService::removeEntry);

        houseRepository.delete(house);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(RegisterHouseDTO info, House house, User owner) {
        house.setAddress(info.getAddress());
        house.setHouseNumber(info.getHouseNumber());
        house.setTelephone(info.getTelephone());
        house.setOwner(owner);

        houseRepository.save(house);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateOwner(House house, User owner) {
        house.setOwner(owner);
        houseRepository.save(house);
    }

    @Override
    public List<User> getListOfHabitants(House house) {
        return house.getUsers();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateHabitants(House newHouse, House oldHouse){
        if (oldHouse != null) {
            oldHouse.setNumberOfHabitants(oldHouse.getNumberOfHabitants() - 1);
            houseRepository.save(oldHouse);
        }

        if (newHouse != null) {
            newHouse.setNumberOfHabitants(newHouse.getNumberOfHabitants() + 1);
            houseRepository.save(newHouse);
        }
    }
}
