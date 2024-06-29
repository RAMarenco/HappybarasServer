package org.happybaras.server.utils;

import org.happybaras.server.domain.dtos.*;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.Token;
import org.happybaras.server.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DTOConverters {
    public PagedHouseDTO convertToPagedHouseDTO(Page<House> housePaged) {
        PagedHouseDTO pagedHouseDTO = new PagedHouseDTO();
        List<FindHouseDTO> findHouseDTOs = new ArrayList<>();

        pagedHouseDTO.setTotalPages(housePaged.getTotalPages());
        pagedHouseDTO.setTotalElements(housePaged.getNumberOfElements());
        pagedHouseDTO.setPage(housePaged.getNumber() + 1);

        housePaged.getContent().forEach(house ->
                findHouseDTOs.add(convertToFindHouseDTO(house))
        );

        pagedHouseDTO.setHouses(findHouseDTOs);

        return pagedHouseDTO;
    }

    public PagedUsersDTO convertToPagedUserDTO(Page<User> userPaged) {
        PagedUsersDTO pagedUsersDTO = new PagedUsersDTO();
        List<FindUserDTO> findUserDTOs = new ArrayList<>();

        pagedUsersDTO.setTotalPages(userPaged.getTotalPages());
        pagedUsersDTO.setTotalElements(userPaged.getNumberOfElements());
        pagedUsersDTO.setPage(userPaged.getNumber() + 1);
        userPaged.getContent().forEach(user ->
                findUserDTOs.add(convertToFindUserDTO(user))
        );

        pagedUsersDTO.setUsers(findUserDTOs);

        return pagedUsersDTO;
    }

    public FindHouseDTO convertToFindHouseDTO(House house) {
        FindHouseDTO findHouseDTO = new FindHouseDTO();
        OwnerDTO ownerDTO = new OwnerDTO();

        findHouseDTO.setId(house.getId());
        findHouseDTO.setAddress(house.getAddress());
        if (house.getOwner() != null) {
            ownerDTO.setId(house.getOwner().getId());
            ownerDTO.setName(house.getOwner().getUsername());
            findHouseDTO.setOwner(ownerDTO);
        }

        findHouseDTO.setHouseNumber(house.getHouseNumber());
        findHouseDTO.setTelephone(house.getTelephone());
        findHouseDTO.setNumberOfHabitants(house.getNumberOfHabitants());

        return findHouseDTO;
    }

    public FindResidentDTO convertToFindResidentDTO(User user) {
        FindResidentDTO findResidentDTO = new FindResidentDTO();

        findResidentDTO.setId(user.getId());
        findResidentDTO.setUsername(user.getUsername());
        findResidentDTO.setEmail(user.getEmail());
        findResidentDTO.setAvatar(user.getAvatar());
        findResidentDTO.setRol(user.getRoles());

        return findResidentDTO;
    }

    public FindUserDTO convertToFindUserDTO(User user) {
        FindUserDTO findUserDTO = new FindUserDTO();

        findUserDTO.setId(user.getId());
        findUserDTO.setUsername(user.getUsername());
        findUserDTO.setEmail(user.getEmail());

        findUserDTO.setRol(user.getRoles());

        findUserDTO.setAddress(convertToAddressDTO(user.getHouse()));
        return findUserDTO;
    }

    public AddressDTO convertToAddressDTO(House house) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(house.getId());
        addressDTO.setAddress(house.getAddress() + " #" + house.getHouseNumber());
        return addressDTO;
    }

    public LoginResponseDTO convertToLoginResponseDTO(Token token, User user) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setContent(token.getContent());
        loginResponseDTO.setAvatar(user.getAvatar());
        return loginResponseDTO;
    }
}
