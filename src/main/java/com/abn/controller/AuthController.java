package com.abn.controller;

import com.abn.dto.JwtTokenDTO;
import com.abn.dto.ResponseMsgDTO;
import com.abn.dto.SingUpDTO;
import com.abn.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Api(tags = "Auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> login(@ApiParam("Username") @RequestParam String username,
                                             @ApiParam("Password") @RequestParam String password) {
        return ResponseEntity.ok(userService.signIn(username, password));
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtTokenDTO> signUp(@ApiParam("Signup User") @Valid @RequestBody SingUpDTO user) {
        return ResponseEntity.ok(userService.signUp(user));
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<JwtTokenDTO> refresh(HttpServletRequest req) {
        return ResponseEntity.ok(userService.refresh(req.getRemoteUser()));
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMsgDTO> delete(@ApiParam("delete user") @PathVariable @NotBlank String username) {
        userService.delete(username);
        return ResponseEntity.ok(ResponseMsgDTO.builder().message("deleted successfully").build());
    }
}
