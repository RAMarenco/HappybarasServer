package org.happybaras.server.services.impl;

import jakarta.transaction.Transactional;
import org.happybaras.server.domain.dtos.PermitRegisterDTO;
import org.happybaras.server.domain.entities.*;
import org.happybaras.server.domain.enums.PermitStatusEnum;
import org.happybaras.server.repositories.PermitRepository;
import org.happybaras.server.services.PermitDayService;
import org.happybaras.server.services.PermitService;
import org.happybaras.server.services.PermitStatusService;
import org.happybaras.server.services.PermitTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PermitServiceImpl implements PermitService {
    private final PermitRepository permitRepository;
    private final PermitStatusService permitStatusService;
    private final PermitTypeService permitTypeService;
    private final PermitDayService permitDayService;

    public PermitServiceImpl(PermitRepository permitRepository, PermitStatusService permitStatusService, PermitTypeService permitTypeService, PermitDayService permitDayService) {
        this.permitRepository = permitRepository;
        this.permitStatusService = permitStatusService;
        this.permitTypeService = permitTypeService;
        this.permitDayService = permitDayService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void create(User visitor, User resident, House house, PermitRegisterDTO info) {
        Permit permit = new Permit();
        List<PermitDay> permitDays = new ArrayList<>();
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.PENDING.value);
        PermitType type = permitTypeService.findByValue(info.getEntranceType());

        permit.setVisitor(visitor);
        permit.setResident(resident);
        permit.setHouse(house);
        permit.setType(type);
        permit.setPeriod(info.getEntrancePeriod());
        permit.setStatus(status);
        permit.setBeginHour(LocalTime.parse(info.getBeginHour()));
        permit.setEndHour(LocalTime.parse(info.getEndHour()));
        permit.setTimestamp(LocalDateTime.now());

        info.getDays().forEach(day -> permitDays.add(permitDayService.save(day, permit)));

        permit.setPermitDays(permitDays);

        permitRepository.save(permit);
    }

    @Override
    public void approve(Permit permit) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        permit.setStatus(status);

        permitRepository.save(permit);
    }

    @Override
    public void reject(Permit permit) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.REJECTED.value);
        permit.setStatus(status);

        permitRepository.save(permit);
    }

    @Override
    public List<Permit> findByResidentOrVisitor(User resident, User visitor) {
        return permitRepository.findAllByResidentOrVisitor(resident, visitor);
    }

    @Override
    public List<Permit> findByResident(User resident) {
        return permitRepository.findAllByResident(resident);
    }

    @Override
    public List<Permit> findLatestPermitRequestsByResident(User resident, LocalDateTime beginDate, LocalDateTime endDate) {
        return permitRepository.findAllByResidentAndTimestampBetween(resident, beginDate, endDate);
    }

    //    TODO: implement pagination
    @Override
    public List<Permit> findByHouse(House house) {
        return permitRepository.findAllByHouse(house);
    }

    @Override
    public List<Permit> findLatestPermitRequestsByHouse(House house, LocalDateTime beginDate, LocalDateTime endDate) {
        return permitRepository.findAllByHouseAndTimestampBetween(house, beginDate, endDate);
    }

    @Override
    public List<Permit> findByVisitorAndStatusApproved(User visitor) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        return permitRepository.findAllByVisitorAndStatus(visitor, status);
    }

    @Override
    public List<Permit> findByVisitorAndStatusApprovedAndDaysBetween(User visitor, LocalDate startDate, LocalDate endDate) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        List<Permit> permits =  permitRepository.findAllByVisitorAndStatus(visitor, status);

        return  permits
                .stream()
                .filter(p -> isPermitDaysBetween(p, startDate, endDate))
                .toList();
    }

    @Override
    public List<Permit> findByVisitorAndStatusApprovedAndDaysAfter(User visitor, LocalDate startDate) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        List<Permit> permits = permitRepository.findAllByVisitorAndStatus(visitor, status);

        return  permits
                .stream()
                .filter(p -> isPermitDaysAfter(p, startDate))
                .toList();
    }

    @Override
    public List<Permit> findByVisitorAndStatusApprovedAndDaysBefore(User visitor, LocalDate endDate) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        List<Permit> permits = permitRepository.findAllByVisitorAndStatus(visitor, status);

        return  permits
                .stream()
                .filter(p -> isPermitDaysBefore(p, endDate))
                .toList();
    }

    @Override
    public List<Permit> findByVisitorAndResidentAndStatusApproved(User visitor, User resident) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        return permitRepository.findAllByVisitorAndStatusAndResident(visitor, status, resident);
    }

    @Override
    public List<Permit> findByVisitorAndResidentAndStatusApprovedAndDaysBetween(User visitor, User resident, LocalDate startDate, LocalDate endDate) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        List<Permit> permits = permitRepository.findAllByVisitorAndStatusAndResident(visitor, status, resident);

        return  permits
                .stream()
                .filter(p -> isPermitDaysBetween(p, startDate, endDate))
                .toList();
    }

    @Override
    public List<Permit> findByVisitorAndResidentAndStatusApprovedAndDaysAfter(User visitor, User resident, LocalDate startDate) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        List<Permit> permits = permitRepository.findAllByVisitorAndStatusAndResident(visitor, status, resident);

        return  permits
                .stream()
                .filter(p -> isPermitDaysAfter(p, startDate))
                .toList();
    }

    @Override
    public List<Permit> findByVisitorAndResidentAndStatusApprovedAndDaysBefore(User visitor, User resident, LocalDate endDate) {
        PermitStatus status = permitStatusService.findByValue(PermitStatusEnum.APPROVED.value);
        List<Permit> permits = permitRepository.findAllByVisitorAndStatusAndResident(visitor, status, resident);

        return  permits
                .stream()
                .filter(p -> isPermitDaysBefore(p, endDate))
                .toList();
    }

    private boolean isPermitDaysBetween(Permit permit, LocalDate startDate, LocalDate endDate) {
        List<PermitDay> days = permit.getPermitDays();

        for(PermitDay day : days) {
            if(day.getDay().isAfter(startDate) && day.getDay().isBefore(endDate))
                return true;
        }

        return false;
    }

    private boolean isPermitDaysAfter(Permit permit, LocalDate startDate) {
        List<PermitDay> days = permit.getPermitDays();

        for(PermitDay day : days) {
            if(day.getDay().isAfter(startDate))
                return true;
        }

        return false;
    }

    private boolean isPermitDaysBefore (Permit permit, LocalDate endDate) {
        List<PermitDay> days = permit.getPermitDays();

        for(PermitDay day : days) {
            if(day.getDay().isBefore(endDate))
                return true;
        }

        return false;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void removePermit(Permit permit) {
        permit.getPermitDays().forEach(permitDayService::removePermitDay);
        permitRepository.delete(permit);
    }
}
