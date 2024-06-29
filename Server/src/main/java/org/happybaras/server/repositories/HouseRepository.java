package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HouseRepository extends JpaRepository<House, UUID> {
    Optional<House> findByHouseNumberEquals(String houseNumber);
    Optional<House> findByOwner(User user);

}
