package org.happybaras.server.services.impl;

import org.happybaras.server.domain.entities.UserRole;
import org.happybaras.server.repositories.UserRoleRepository;
import org.happybaras.server.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserRole> findAllRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    public UserRole findRole(String role) {
        return userRoleRepository.findById(role).orElse(null);
    }
}
