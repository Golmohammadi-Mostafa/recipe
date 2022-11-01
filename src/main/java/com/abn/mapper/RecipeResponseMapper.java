package com.abn.mapper;

import com.abn.dto.RecipeResponseDTO;
import com.abn.entity.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeResponseMapper extends EntityMapper<RecipeResponseDTO, Recipe> {
}
