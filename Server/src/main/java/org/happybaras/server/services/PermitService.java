package org.happybaras.server.services;

import org.happybaras.server.domain.dtos.PermitRegisterDTO;
import org.happybaras.server.domain.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PermitService {
//    TODO: add methods for validating permits
    void create(User visitor, User resident, House house, PermitRegisterDTO info);
    void approve(Permit permit);
    void reject(Permit permit);
    // Normal resident
    List<Permit> findByResidentOrVisitor(User resident, User visitor);
    List<Permit> findByResident(User resident);
    List<Permit> findLatestPermitRequestsByResident(User resident, LocalDateTime beginDate, LocalDateTime endDate);
    // Main resident
    List<Permit> findByHouse(House house);
    List<Permit> findLatestPermitRequestsByHouse(House house, LocalDateTime beginDate, LocalDateTime endDate);
    // Visitor
    List<Permit> findByVisitorAndStatusApproved(User visitor);
    List<Permit> findByVisitorAndStatusApprovedAndDaysBetween(User visitor, LocalDate startDate, LocalDate endDate);
    List<Permit> findByVisitorAndStatusApprovedAndDaysAfter(User visitor, LocalDate startDate);
    List<Permit> findByVisitorAndStatusApprovedAndDaysBefore(User visitor, LocalDate endDate);
    List<Permit> findByVisitorAndResidentAndStatusApproved(User visitor, User resident);
    List<Permit> findByVisitorAndResidentAndStatusApprovedAndDaysBetween(User visitor, User resident, LocalDate startDate, LocalDate endDate);
    List<Permit> findByVisitorAndResidentAndStatusApprovedAndDaysAfter(User visitor, User resident, LocalDate startDate);
    List<Permit> findByVisitorAndResidentAndStatusApprovedAndDaysBefore(User visitor, User resident, LocalDate endDate);

    void removePermit(Permit permit);

}
