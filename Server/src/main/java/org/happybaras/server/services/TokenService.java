package org.happybaras.server.services;

import org.happybaras.server.domain.entities.Token;

public interface TokenService {
    void removeToken(Token token);
}
