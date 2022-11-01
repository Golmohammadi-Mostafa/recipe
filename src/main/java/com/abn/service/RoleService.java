package com.abn.service;

import com.abn.entity.Role;
import com.abn.enums.RoleType;

import java.util.Optional;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
public interface RoleService {
    Optional<Role> findByName(RoleType roleName);
}
