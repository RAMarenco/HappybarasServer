package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, UUID> {
    Optional<Code> findByCode(String code);
    Optional<Code> findByCodeAndValid(String code, boolean valid);
}
