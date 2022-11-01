package com.abn.mapper;

import com.abn.dto.IngredientDTO;
import com.abn.entity.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {
}
