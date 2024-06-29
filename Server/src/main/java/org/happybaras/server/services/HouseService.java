package org.happybaras.server.services;

import org.happybaras.server.domain.dtos.AddressDTO;
import org.happybaras.server.domain.dtos.PagedHouseDTO;
import org.happybaras.server.domain.dtos.RegisterHouseDTO;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface HouseService {
    List<House> findAll();
    PagedHouseDTO findHouses(int page);
    int getTotalPages(int page);
    House findByHouseNumber(String number);
    House findByOwner(User user);
    House findById(UUID id);
    List<AddressDTO> findAllAddresses();
    void create(RegisterHouseDTO info, User owner);
    void delete(House house);
    void update(RegisterHouseDTO info, House house, User owner);
    void updateOwner(House house, User owner);
    List<User> getListOfHabitants(House house);
    void updateHabitants(House newHouse, House oldHouse);
}
