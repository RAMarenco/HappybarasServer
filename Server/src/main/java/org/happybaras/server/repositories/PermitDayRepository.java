package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.PermitDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermitDayRepository extends JpaRepository<PermitDay, UUID> {
}
