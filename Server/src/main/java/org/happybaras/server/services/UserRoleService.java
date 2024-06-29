package org.happybaras.server.services;

import org.happybaras.server.domain.entities.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findAllRoles();

    UserRole findRole(String role);
}
