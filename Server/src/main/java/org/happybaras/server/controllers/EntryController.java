package org.happybaras.server.controllers;

import jakarta.validation.Valid;
import org.happybaras.server.domain.dtos.GeneralResponse;
import org.happybaras.server.domain.dtos.RegisterEntryDTO;
import org.happybaras.server.domain.entities.Entry;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.services.EntryService;
import org.happybaras.server.services.HouseService;
import org.happybaras.server.services.UserService;
import org.happybaras.server.utils.EntriesFilters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/api/entry")
public class EntryController {
    private final EntryService entryService;
    private final UserService userService;
    private final HouseService houseService;
    private final EntriesFilters entriesFilters;

    public EntryController(EntryService entryService, UserService userService, HouseService houseService, EntriesFilters entriesFilters) {
        this.entryService = entryService;
        this.userService = userService;
        this.houseService = houseService;
        this.entriesFilters = entriesFilters;
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> registerNewEntry(@RequestBody @Valid RegisterEntryDTO info) {
        User user = userService.findOneByIdentifier(info.getIdentifier());
        if(user == null)
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();

        House house = houseService.findByHouseNumber(info.getHouseNumber());
        if(house == null)
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();

        User vigilant = null;
        try {
            vigilant = userService.findUserAuthenticated();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(vigilant == null)
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();

        entryService.registerEntry(user, house, vigilant, info);
        return GeneralResponse.builder().status(HttpStatus.OK).message("Entrada registrada").getResponse();
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> getAll() {
        return GeneralResponse.builder().status(HttpStatus.OK).data(entryService.findAll()).getResponse();
    }

    @GetMapping("/byMonth")
    public ResponseEntity<GeneralResponse> getSumByMonths() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0); // year-1-1T00:00
        LocalDateTime endDate = LocalDateTime.of(now.getYear(), 12, 31, 23, 59); // year-12-31T23:59
        List<Entry> entries = entryService.findByPeriod(beginDate, endDate);

        return GeneralResponse.builder().data(entriesFilters.mapEntriesByMonths(entries)).status(HttpStatus.OK).getResponse();
    }

    @GetMapping("/byWeek")
    public ResponseEntity<GeneralResponse> getSumByWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDate;
        LocalDateTime endDate;

        // When Monday the dashboard will show only the entries of Monday
        // It will be showing the entries of each day of the week until it reaches Sunday and then the cycle repeats
        switch (now.getDayOfWeek()) {
            case MONDAY -> {
                beginDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
                endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59).plusDays(6);
            }
            case TUESDAY -> {
                beginDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(1);
                endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59).plusDays(5);
            }
            case WEDNESDAY -> {
                beginDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(2);
                endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59).plusDays(4);
            }
            case THURSDAY -> {
                beginDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(3);
                endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59).plusDays(3);
            }
            case FRIDAY -> {
                beginDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(4);
                endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59).plusDays(2);
            }
            case SATURDAY -> {
                beginDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(5);
                endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59).plusDays(1);
            }
            case SUNDAY -> {
                beginDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(6);
                endDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59);
            }
            default -> {
                return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
            }
        }

        List<Entry> entries = entryService.findByPeriod(beginDate, endDate);

        return GeneralResponse.builder().data(entriesFilters.mapEntriesByWeek(entries)).status(HttpStatus.OK).getResponse();

    }
    @GetMapping("/byYear")
    public ResponseEntity<GeneralResponse> getSumByYears() {
        LocalDateTime beginDate = LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, Month.DECEMBER, 31, 23, 59);

        List<Entry> entries = entryService.findByPeriod(beginDate, endDate);

        return GeneralResponse.builder().data(entriesFilters.mapEntriesByYear(entries)).status(HttpStatus.OK).getResponse();
    }

//    TODO: Implementar paginación para obtener el registro de entradas realizadas a la casa
//    TODO: Implementar filtros para obtener los registros de entradas realizadas a la casa dependiendo de si son visitantes o residentes
    @GetMapping("/byHouse")
    public ResponseEntity<GeneralResponse> getByHouse() {
        User user = null;
        try {
            user = userService.findUserAuthenticated();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(user == null)
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();

        House house = houseService.findByOwner(user);

        if(house == null)
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();

        List<Entry> entries = entryService.findByHouse(house);

        return GeneralResponse.builder().data(entries).status(HttpStatus.OK).getResponse();
    }

}
