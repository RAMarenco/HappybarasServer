package org.happybaras.server.repositories;

import org.happybaras.server.domain.entities.Token;
import org.happybaras.server.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token> findByUser(User user);
}
