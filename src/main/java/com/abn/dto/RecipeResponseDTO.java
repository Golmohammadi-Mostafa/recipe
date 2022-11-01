package com.abn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class RecipeResponseDTO implements Serializable {
    private Long id;
    private String name;
    private Integer serves;
    private List<IngredientDTO> ingredients;
    private Boolean vegetarian;
    private String instructions;
}


