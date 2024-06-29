package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findOneByUsernameOrEmail(String username, String email);
    List<User> findByHouse(House house);
}
