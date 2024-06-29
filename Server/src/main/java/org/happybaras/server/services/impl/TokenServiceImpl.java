package org.happybaras.server.services.impl;

import jakarta.transaction.Transactional;
import org.happybaras.server.domain.entities.Token;
import org.happybaras.server.repositories.TokenRepository;
import org.happybaras.server.services.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void removeToken(Token token) {
        tokenRepository.delete(token);
    }
}
