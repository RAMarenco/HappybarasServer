package org.happybaras.server.controllers;

import jakarta.validation.Valid;
import org.happybaras.server.domain.dtos.EmailDTO;
import org.happybaras.server.domain.dtos.GeneralResponse;
import org.happybaras.server.domain.dtos.PermitRegisterDTO;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.Permit;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.services.HouseService;
import org.happybaras.server.services.PermitService;
import org.happybaras.server.services.UserService;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permit")
public class PermitController {
    private final PermitService permitService;
    private final UserService userService;
    private final HouseService houseService;

    public PermitController(PermitService permitService, UserService userService, HouseService houseService) {
        this.permitService = permitService;
        this.userService = userService;
        this.houseService = houseService;
    }

//    TODO: add pagination for visitor permits
    @GetMapping("/all-by-visitor")
    @PreAuthorize("hasAuthority('VISITOR')")
    public ResponseEntity<GeneralResponse>
    findByVisitorAndApproved(
            @RequestParam (value = "email", required = false) String email,
            @RequestParam (value = "startDate", required = false)String startDate,
            @RequestParam (value = "endDate", required = false) String endDate) {

        User visitor = null;
        List<Permit> permits = null;

        try {
            visitor = userService.findUserAuthenticated();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // FILTERS
        if(startDate != null && endDate != null && email != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            if(start.isAfter(end))
                return GeneralResponse.builder().status(HttpStatus.BAD_REQUEST).getResponse();

            User resident = userService.findOneByIdentifier(email);

            permits = permitService.findByVisitorAndResidentAndStatusApprovedAndDaysBetween(visitor, resident, start, end);
        }
        else if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);

            if(start.isAfter(end))
                return GeneralResponse.builder().status(HttpStatus.BAD_REQUEST).getResponse();

            permits = permitService.findByVisitorAndStatusApprovedAndDaysBetween(visitor, start, end);
        }
        else if (startDate != null && email != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            User resident = userService.findOneByIdentifier(email);

            permits = permitService.findByVisitorAndResidentAndStatusApprovedAndDaysAfter(visitor, resident, start);
        }
        else if(endDate != null && email != null) {
            User resident = userService.findOneByIdentifier(email);
            LocalDate end = LocalDate.parse(endDate, formatter);

            permits = permitService.findByVisitorAndResidentAndStatusApprovedAndDaysBefore(visitor, resident, end);
        }
        else if (startDate != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);

            permits = permitService.findByVisitorAndStatusApprovedAndDaysAfter(visitor, start);
        }
        else if (endDate != null) {
            LocalDate end = LocalDate.parse(endDate, formatter);

            permits = permitService.findByVisitorAndStatusApprovedAndDaysBefore(visitor, end);
        }
        else if (email != null) {
            User resident = userService.findOneByIdentifier(email);
            permits = permitService.findByVisitorAndResidentAndStatusApproved(visitor, resident);
        }
        else
            permits = permitService.findByVisitorAndStatusApproved(visitor);


        return GeneralResponse.builder().data(permits).message("Permits found").getResponse();
    }

//    TODO: add pagination in this one as well
    @GetMapping("/all-by-house/{house_id}")
    @PreAuthorize("hasAuthority('MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> findByHouse(@PathVariable UUID house_id) {
        House house = houseService.findById(house_id);

        if(house == null)
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("House not found").getResponse();

        List<Permit> permits = permitService.findByHouse(house);

        return GeneralResponse.builder().data(permits).message("Permits found").getResponse();
    }

//    TODO: add pagination in this one as well
    @GetMapping("/all-by-resident")
    @PreAuthorize("hasAnyAuthority('NORMALRESIDENT', 'MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> findByResident() {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

    @GetMapping("/latest-by-house")
    @PreAuthorize("hasAuthority('MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> findLatestByHouse() {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

    @GetMapping("/latest-by-resident")
    @PreAuthorize("hasAnyAuthority('NORMALRESIDENT', 'MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> findLatestByResident() {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('NORMALRESIDENT', 'MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> register(@RequestBody @Valid PermitRegisterDTO info) {
        User visitor = userService.findOneByIdentifier(info.getVisitor());
        User resident = null;
        House house = null;

        if(visitor == null)
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();

        try {
            resident = userService.findUserAuthenticated();
            house = resident.getHouse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        permitService.create(visitor, resident, house, info);
        return GeneralResponse.builder().status(HttpStatus.CREATED).message("Permit registered succesfully").getResponse();
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> approve() {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('MAINRESIDENT')")
    public ResponseEntity<GeneralResponse> reject() {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

}
