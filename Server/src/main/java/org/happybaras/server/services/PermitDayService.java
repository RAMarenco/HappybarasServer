package org.happybaras.server.services;

import org.happybaras.server.domain.entities.Permit;
import org.happybaras.server.domain.entities.PermitDay;

import java.time.LocalDate;

public interface PermitDayService {
    PermitDay save(LocalDate day, Permit permit);

    void removePermitDay(PermitDay permitDay);
}
