package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.PermitStatus;
import org.happybaras.server.domain.entities.PermitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermitTypeRepository extends JpaRepository<PermitType, String> {
    Optional<PermitType> findByType(String type);
}
