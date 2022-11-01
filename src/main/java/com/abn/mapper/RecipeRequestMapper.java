package com.abn.mapper;

import com.abn.dto.RecipeRequestDTO;
import com.abn.entity.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeRequestMapper extends EntityMapper<RecipeRequestDTO, Recipe> {
}
