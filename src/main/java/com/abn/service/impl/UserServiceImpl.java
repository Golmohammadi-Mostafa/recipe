package com.abn.service.impl;

import com.abn.dto.JwtTokenDTO;
import com.abn.dto.SingUpDTO;
import com.abn.entity.Role;
import com.abn.entity.User;
import com.abn.enums.RoleType;
import com.abn.exception.CustomException;
import com.abn.repository.UserRepository;
import com.abn.security.JwtTokenProvider;
import com.abn.service.RoleService;
import com.abn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;


    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager,
                           RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }

    /**
     * This is the method to log in
     *
     * @param username username of user
     * @param password password of user
     * @return this method return jwt token for logged in user
     */
    @Override
    public JwtTokenDTO signIn(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByUsername(username);
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            return new JwtTokenDTO(token);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * @param singUpDTO required field to register new user
     * @return jwt token of user
     */
    @Override
    public JwtTokenDTO signUp(SingUpDTO singUpDTO) {
        if (!userRepository.existsByUsername(singUpDTO.getUsername())) {
            User user = new User();
            Set<RoleType> roleType = singUpDTO.getRoleType();
            Set<Role> roles = new HashSet<>();
            user.setUsername(singUpDTO.getUsername());
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode(singUpDTO.getPassword()));
            userRepository.save(user);
            roleType.forEach(r -> {
                Optional<Role> role = roleService.findByName(r);
                role.ifPresent(roles::add);
            });
            User byUsername = userRepository.findByUsername(singUpDTO.getUsername());
            byUsername.setRoles(roles);
            User savedUser = userRepository.save(byUsername);
            String token = jwtTokenProvider.createToken(savedUser.getUsername(), savedUser.getRoles());
            return new JwtTokenDTO(token);
        } else {
            throw new CustomException("username already exist", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.info("request to delete user by username: {}", username);
        if (!userRepository.existsByUsername(username)) {
            throw new CustomException("username not found", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteByUsername(username);
    }

    @Override
    public JwtTokenDTO refresh(String username) {
        String token = jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setToken(token);
        return jwtTokenDTO;
    }


}
