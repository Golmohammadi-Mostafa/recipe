package com.abn.dto;

import com.abn.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingUpDTO implements Serializable {

    @NotBlank(message = "username can't be null or empty")
    private String username;
    @NotBlank(message = "username can't be null or empty")
    private String password;
    @NotNull
    private Set<RoleType> roleType;
}
