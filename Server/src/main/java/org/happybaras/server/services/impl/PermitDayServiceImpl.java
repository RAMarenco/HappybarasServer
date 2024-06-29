package org.happybaras.server.services.impl;

import jakarta.transaction.Transactional;
import org.happybaras.server.domain.entities.Permit;
import org.happybaras.server.domain.entities.PermitDay;
import org.happybaras.server.repositories.PermitDayRepository;
import org.happybaras.server.services.PermitDayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PermitDayServiceImpl implements PermitDayService {
    private final PermitDayRepository permitDayRepository;

    public PermitDayServiceImpl(PermitDayRepository permitDayRepository) {
        this.permitDayRepository = permitDayRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public PermitDay save(LocalDate day, Permit permit) {
        PermitDay permitDay = new PermitDay();
        permitDay.setDay(day);
        permitDay.setPermit(permit);
        permitDayRepository.save(permitDay);
        return permitDay;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void removePermitDay(PermitDay permitDay) {
        permitDay.setPermit(null);
        permitDayRepository.delete(permitDay);
    }
}
