package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
}
