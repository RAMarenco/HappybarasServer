package org.happybaras.server.services;

import org.happybaras.server.domain.dtos.FindResidentDTO;
import org.happybaras.server.domain.dtos.LoginDTO;
import org.happybaras.server.domain.dtos.PagedHouseDTO;
import org.happybaras.server.domain.dtos.PagedUsersDTO;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.Token;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.domain.entities.UserRole;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // Token management
    Token registerToken(User user) throws Exception;

    Boolean isTokenValid(User user, String token);

    void cleanTokens(User user) throws Exception;

    User findUserAuthenticated() throws Exception;

    User findOneByIdentifier(String identifier);

    User findByUsernameOrEmail(LoginDTO info);

    PagedUsersDTO findUsers(int page);

    User findUserById(UUID id);

    List<FindResidentDTO> findHouseResidents(House house);

    void createUser(LoginDTO info);

    void updateUser(User user, UserRole role, House house);

    void deleteUser(User user);

    void changeRoles(User user, List<String> roles);

    void removeHouse(User user);
}
