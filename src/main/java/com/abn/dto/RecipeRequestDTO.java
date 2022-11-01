package com.abn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeRequestDTO implements Serializable {

    @NotBlank(message = "name can't be null or empty")
    private String name;
    @NotNull(message = "serves can't be null or empty")
    private Integer serves;
    @NotNull
    private List<IngredientDTO> ingredients;
    @NotNull(message = "vegetarian can't be null or empty")
    private Boolean vegetarian;
    @NotBlank(message = "instructions can't be null or empty")
    private String instructions;
}


