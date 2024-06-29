package org.happybaras.server.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.happybaras.server.domain.dtos.*;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.services.HouseService;
import org.happybaras.server.services.UserService;
import org.happybaras.server.utils.DTOConverters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/house")
public class HouseController {
    private final HouseService houseService;
    private final UserService userService;
    private final DTOConverters dtoConverters;

    public HouseController(HouseService houseService, UserService userService, DTOConverters dtoConverters) {
        this.houseService = houseService;
        this.userService = userService;
        this.dtoConverters = dtoConverters;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> findAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        PagedHouseDTO houses = houseService.findHouses(page - 1);

        if (houses.getHouses().isEmpty()) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("Houses not found.").getResponse();
        }

        return GeneralResponse.builder().status(HttpStatus.OK).message("Houses found.").data(houses).getResponse();
    }

    @GetMapping("/addresses")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> findAddresses() {
        List<AddressDTO> addresses = houseService.findAllAddresses();

        if (addresses.isEmpty()) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("Addresses not found.").getResponse();
        }

        return GeneralResponse.builder().status(HttpStatus.OK).message("Addresses found.").data(addresses).getResponse();
    }

    @GetMapping("/pages")
    public ResponseEntity<GeneralResponse> findPages() {
        int pages = houseService.getTotalPages(0);
        PagesDTO pagesDTO = new PagesDTO(pages);
        return GeneralResponse.builder().status(HttpStatus.OK).data(pagesDTO).getResponse();
    }

    @GetMapping("/find-by-number")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> findByHouseNumber(@RequestBody @Valid HouseNumberDTO info) {
        List<FindHouseDTO> houseList = new ArrayList<>();
        House house = houseService.findByHouseNumber(info.getHouseNumber());

        if (house == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("House with this number not found.").getResponse();
        }

        FindHouseDTO houseConverted = dtoConverters.convertToFindHouseDTO(house);

        houseList.add(houseConverted);

        return GeneralResponse.builder().status(HttpStatus.OK).message("House found.").data(houseList).getResponse();
    }

    @GetMapping("/find-residents")
    @PreAuthorize("hasAnyAuthority('NORMALRESIDENT', 'MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> findHabitants() {
        User user;

        try {
            user = userService.findUserAuthenticated();
        } catch (Exception e) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
        }

        List<FindResidentDTO> residents = userService.findHouseResidents(user.getHouse())
                .stream().filter(userFilter -> !userFilter.getEmail().equals(user.getEmail()))
                .toList();

        if (residents.isEmpty()) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("Residents not found.").getResponse();
        }

        return GeneralResponse.builder().status(HttpStatus.OK).message("Residents found.").data(residents).getResponse();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> create(@RequestBody @Valid RegisterHouseDTO info) {
        House verifyNumber = houseService.findByHouseNumber(info.getHouseNumber());

        if (verifyNumber != null) {
            return GeneralResponse.builder().status(HttpStatus.CONFLICT).message("This house number already exists.").getResponse();
        }

        User owner = userService.findOneByIdentifier(info.getOwnerEmail());

        if (owner == null && info.getOwnerEmail() != null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("User with this email not found.").getResponse();
        }

        houseService.create(info, owner);

        return GeneralResponse.builder().status(HttpStatus.OK).message("House created successfully.").getResponse();
    }

    @PutMapping("/update/{house_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> update(@PathVariable UUID house_id, @RequestBody @Valid RegisterHouseDTO info) {
        House house = houseService.findById(house_id);

        if (house == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("House with this id not found.").getResponse();
        }

        House verifyNumber = houseService.findByHouseNumber(info.getHouseNumber());

        if (verifyNumber != null && !verifyNumber.equals(house)) {
            return GeneralResponse.builder().status(HttpStatus.CONFLICT).message("This house number already exists.").getResponse();
        }

        User owner = userService.findOneByIdentifier(info.getOwnerEmail());

        if (owner == null && info.getOwnerEmail() != null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("User with this email not found.").getResponse();
        }

        houseService.update(info, house, owner);

        return GeneralResponse.builder().status(HttpStatus.OK).message("House updated successfully.").getResponse();
    }

    @DeleteMapping("/delete/{house_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> delete(@PathVariable UUID house_id) {
        House house = houseService.findById(house_id);

        if (house == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("House with this id not found.").getResponse();
        }

        houseService.delete(house);

        return GeneralResponse.builder().status(HttpStatus.OK).message("House deleted successfully.").getResponse();
    }
}
