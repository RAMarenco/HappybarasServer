package org.happybaras.server.controllers;

import jakarta.validation.Valid;
import org.happybaras.server.domain.dtos.*;
import org.happybaras.server.domain.entities.House;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.domain.entities.UserRole;
import org.happybaras.server.services.HouseService;
import org.happybaras.server.services.UserRoleService;
import org.happybaras.server.services.UserService;
import org.happybaras.server.utils.DTOConverters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final HouseService houseService;
    private final DTOConverters dtoConverters;

    public UserController(UserService userService, UserRoleService userRoleService, DTOConverters dtoConverters, HouseService houseService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.dtoConverters = dtoConverters;
        this.houseService = houseService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> findAll(@RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        PagedUsersDTO users = userService.findUsers(page - 1);

        if (users.getUsers().isEmpty()) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("Users not found.").getResponse();
        }

        return GeneralResponse.builder().status(HttpStatus.OK).message("Users found.").data(users).getResponse();
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> findAllRoles(@RequestParam(name = "page", required = false, defaultValue = "1") int page) {
        List<UserRole> roles = userRoleService.findAllRoles();

        if (roles.isEmpty()) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("Roles not found.").getResponse();
        }

        return GeneralResponse.builder().status(HttpStatus.OK).message("Roles found.").data(roles).getResponse();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> findByEmail(@RequestBody @Valid EmailDTO info) {
        FindUserDTO user = dtoConverters.convertToFindUserDTO(userService.findOneByIdentifier(info.getEmail()));

        if (user == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("User not found.").getResponse();
        }

        return GeneralResponse.builder().status(HttpStatus.OK).message("User found.").data(user).getResponse();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> delete(@RequestBody @Valid UUIDDTO info) {
        User user = userService.findUserById(info.getId());

        if (user == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("User not found.").getResponse();
        }

        userService.deleteUser(user);

        return GeneralResponse.builder().status(HttpStatus.OK).message("User deleted successfully").getResponse();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> update(@RequestBody @Valid UserEditionDTO info) {
        User user = userService.findUserById(info.getUser());

        if (user == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("User not found.").getResponse();
        }

        UserRole role = userRoleService.findRole(info.getRole());

        if (role == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("Role not found.").getResponse();
        }

        House house = houseService.findById(info.getHouse());

        if (house == null) {
            return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("House not found.").getResponse();
        }

        userService.updateUser(user, role, house);

        return GeneralResponse.builder().status(HttpStatus.OK).message("User updated successfully.").getResponse();
    }

}
