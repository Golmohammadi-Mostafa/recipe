package com.abn.service;

import com.abn.dto.JwtTokenDTO;
import com.abn.dto.SingUpDTO;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
public interface UserService {
    JwtTokenDTO signIn(String username, String password);

    JwtTokenDTO signUp(SingUpDTO dto);

    void delete(String username);

    JwtTokenDTO refresh(String username);

}
