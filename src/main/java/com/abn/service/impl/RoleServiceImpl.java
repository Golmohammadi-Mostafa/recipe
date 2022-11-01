package com.abn.service.impl;

import com.abn.entity.Role;
import com.abn.enums.RoleType;
import com.abn.repository.RoleRepository;
import com.abn.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByName(RoleType roleName) {
        return roleRepository.findByName(roleName);
    }
}
