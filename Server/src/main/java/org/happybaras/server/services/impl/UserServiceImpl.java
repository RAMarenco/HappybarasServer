package org.happybaras.server.services.impl;

import jakarta.transaction.Transactional;
import org.happybaras.server.domain.dtos.FindResidentDTO;
import org.happybaras.server.domain.dtos.LoginDTO;
import org.happybaras.server.domain.dtos.PagedUsersDTO;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.Token;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.domain.entities.UserRole;
import org.happybaras.server.repositories.TokenRepository;
import org.happybaras.server.repositories.UserRepository;
import org.happybaras.server.repositories.UserRoleRepository;
import org.happybaras.server.services.*;
import org.happybaras.server.utils.DTOConverters;
import org.happybaras.server.utils.JWTTools;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JWTTools jwtTools;
    private final TokenRepository tokenRepository;
    private final UserRoleRepository userRoleRepository;
    private final HouseService houseService;
    private final PermitService permitService;
    private final EntryService entryService;
    private final TokenService tokenService;
    private final DTOConverters dtoConverters;

    public UserServiceImpl(JWTTools jwtTools, TokenRepository tokenRepository, UserRepository userRepository, UserRoleRepository userRoleRepository, DTOConverters dtoConverters, @Lazy HouseService houseService, PermitService permitService, EntryService entryService, TokenService tokenService) {
        this.jwtTools = jwtTools;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.dtoConverters = dtoConverters;
        this.houseService = houseService;
        this.permitService = permitService;
        this.entryService = entryService;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Token registerToken(User user) throws Exception {
        cleanTokens(user);

        String tokenString = jwtTools.generateToken(user);
        Token token = new Token(tokenString, user);

        tokenRepository.save(token);

        return token;
    }

    @Override
    public Boolean isTokenValid(User user, String token) {
        try {
            cleanTokens(user);
            List<Token> tokens = tokenRepository.findByUser(user);

            tokens.stream()
                    .filter(tk -> tk.getContent().equals(token))
                    .findAny()
                    .orElseThrow(Exception::new);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void cleanTokens(User user) throws Exception {
        List<Token> tokens = tokenRepository.findByUser(user);

        tokens.forEach(token -> {
            if (!jwtTools.verifyToken(token.getContent())) {
                token.setActive(false);
                tokenRepository.save(token);
            }
        });
    }

    @Override
    public User findUserAuthenticated() {
        String identifier = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findOneByUsernameOrEmail(identifier, identifier).orElse(null);
    }

    @Override
    public User findOneByIdentifier(String identifier) {
        return userRepository.findOneByUsernameOrEmail(identifier, identifier).orElse(null);
    }

    @Override
    public User findByUsernameOrEmail(LoginDTO info) {
        return userRepository.findOneByUsernameOrEmail(info.getName(), info.getEmail()).orElse(null);
    }

    @Override
    public PagedUsersDTO findUsers(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return dtoConverters.convertToPagedUserDTO(userRepository.findAll(pageable));
    }

    @Override
    public User findUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<FindResidentDTO> findHouseResidents(House house) {
        return userRepository.findByHouse(house).stream()
                .map(dtoConverters::convertToFindResidentDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createUser(LoginDTO info) {
        User user = new User();

        user.setUsername(info.getName());
        user.setEmail(info.getEmail());
        user.setAvatar(info.getPicture());


        userRepository.save(user);

        List<String> userRoles = new ArrayList<>();
        userRoles.add("VISITOR");

        changeRoles(user, userRoles);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateUser(User user, UserRole role, House house) {
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(role);

        if (role.getId().equals("ADMIN") || role.getId().equals("VISITOR")) {
            houseService.updateHabitants(null, user.getHouse());
            user.setHouse(null);
        } else {
            if (!house.equals(user.getHouse())) {
                houseService.updateHabitants(house, user.getHouse());
                user.setHouse(house);
            }
        }

        user.setRoles(userRoles);

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(User user) {
        user.getPermitsResident().forEach(permitService::removePermit);
        user.getPermitsVisitor().forEach(permitService::removePermit);
        user.getEntries().forEach(entryService::removeEntry);
        user.getTokens().forEach(tokenService::removeToken);

        if (!user.getHouses().isEmpty()) {
            user.getHouses().forEach(house -> houseService.updateOwner(house, user));
        }

        if (user.getHouse() != null) {
            houseService.updateHabitants(null, user.getHouse());
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void changeRoles(User user, List<String> roles) {
        List<UserRole> rolesFound = userRoleRepository.findAllById(roles);
        user.setRoles(rolesFound);

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void removeHouse(User user) {
        user.setHouse(null);
        List<String> userRoles = new ArrayList<>();
        userRoles.add("VISITOR");

        changeRoles(user, userRoles);
    }
}
