package com.abn;

import com.abn.dto.IngredientDTO;
import com.abn.dto.RecipeRequestDTO;
import com.abn.entity.Ingredient;
import com.abn.entity.Recipe;
import com.abn.entity.User;
import com.abn.mapper.RecipeRequestMapper;
import com.abn.repository.RecipeRepository;
import com.abn.repository.UserRepository;
import com.abn.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceUnitTest {
    @Mock
    RecipeRequestMapper mapper;
    @Captor
    ArgumentCaptor<Recipe> captor;
    @InjectMocks
    private RecipeServiceImpl underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RecipeRepository recipeRepository;

    @Test
    void createRecipe_saveRecipe_returnNewRecipe() {

        //given

        User user = new User();
        user.setUsername("Jose");
        user.setPassword("123");
        user.setRoles(new HashSet<>());

        given(userRepository.findByUsername(any(String.class))).willReturn(user);

        List<IngredientDTO> ingredients = new ArrayList<>();
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setName("garlic");
        ingredients.add(ingredientDTO);

        RecipeRequestDTO recipeRequestDTO = new RecipeRequestDTO();
        recipeRequestDTO.setName("Sweet Potato and Red Onion");
        recipeRequestDTO.setIngredients(ingredients);
        recipeRequestDTO.setServes(4);
        recipeRequestDTO.setInstructions("Roast in the oven for about 20 minutes.");
        recipeRequestDTO.setVegetarian(true);


        Recipe recipe = new Recipe();
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName());
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(ingredient);
        recipe.setInstructions(recipeRequestDTO.getInstructions());
        recipe.setUser(user);
        recipe.setServes(recipeRequestDTO.getServes());
        recipe.setVegetarian(recipeRequestDTO.getVegetarian());
        recipe.setName(recipeRequestDTO.getName());
        recipe.setIngredients(ingredientList);

        given(mapper.toEntity(any(RecipeRequestDTO.class))).willReturn(recipe);
        given(recipeRepository.save(any(Recipe.class))).willReturn(recipe);

        //when
        underTest.create(recipeRequestDTO, user.getUsername());

        //assert
        verify(recipeRepository).save(captor.capture());
        Recipe captured = captor.getValue();

        assertEquals(recipeRequestDTO.getName(), captured.getName());
        assertEquals(recipeRequestDTO.getServes(), captured.getServes());
        assertEquals(recipeRequestDTO.getVegetarian(), captured.getVegetarian());
    }
}
