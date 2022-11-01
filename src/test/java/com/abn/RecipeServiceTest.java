package com.abn;

import com.abn.dto.IngredientDTO;
import com.abn.dto.RecipeRequestDTO;
import com.abn.entity.Ingredient;
import com.abn.entity.Recipe;
import com.abn.entity.User;
import com.abn.repository.RecipeRepository;
import com.abn.repository.UserRepository;
import com.abn.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ActiveProfiles("test")
//@Sql(scripts = {"/db/changelog/sql/01-script-init.sql"})
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeServiceTest {
    @Autowired
    private RecipeServiceImpl recipeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void deleteRecords() {
        userRepository.deleteAll();
    }

    @Test
    void createRecipe_saveRecipe_returnNewRecipe() {

        //given

        User user = new User();
        user.setUsername("Jose");
        user.setPassword("123");
        user.setRoles(new HashSet<>());
        userRepository.save(user);

        List<IngredientDTO> ingredients = new ArrayList<>();
        IngredientDTO ingredient1 = new IngredientDTO();
        ingredient1.setName("garlic");
        IngredientDTO ingredient2 = new IngredientDTO();
        ingredient2.setName("ginger");
        IngredientDTO ingredient3 = new IngredientDTO();
        ingredient3.setName("potato");

        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);

        RecipeRequestDTO recipeRequestDTO = new RecipeRequestDTO();
        recipeRequestDTO.setName("Sweet Potato and Red Onion");
        recipeRequestDTO.setIngredients(ingredients);
        recipeRequestDTO.setServes(4);
        recipeRequestDTO.setInstructions("Roast in the oven for about 20 minutes or until the potatoes and the sweet potatoes are fork tender");
        recipeRequestDTO.setVegetarian(true);

        //when
        Recipe recipe = recipeService.create(recipeRequestDTO, user.getUsername());

        //assert
        assertEquals(recipe.getName(), recipeRequestDTO.getName());
        assertEquals(recipe.getVegetarian(), true);
        assertEquals(recipe.getServes(), 4);
        assertEquals(recipe.getUser().getUsername(), user.getUsername());
    }

    @Test
    void updateRecipe_commitChanges_returnUpdatedRecipe() {

        User user = new User();
        user.setUsername("Jose");
        user.setPassword("123");
        user.setRoles(new HashSet<>());
        user = userRepository.save(user);

        Recipe recipe = new Recipe();
        recipe.setUser(user);
        Ingredient ingredient = new Ingredient();
        ingredient.setName("garlic");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);
        recipe.setServes(2);
        recipe.setVegetarian(true);
        recipe.setInstructions("Roast in the oven for about 20 minutes");
        recipe.setName("Sweet Potato and Red Onion");
        recipe = recipeRepository.save(recipe);


        List<IngredientDTO> ingredientDTOs = new ArrayList<>();
        IngredientDTO ingredient1 = new IngredientDTO();
        ingredient1.setName("garlic");
        IngredientDTO ingredient2 = new IngredientDTO();
        ingredient2.setName("ginger");
        IngredientDTO ingredient3 = new IngredientDTO();
        ingredient3.setName("potato");

        ingredientDTOs.add(ingredient1);
        ingredientDTOs.add(ingredient2);
        ingredientDTOs.add(ingredient3);

        RecipeRequestDTO recipeRequestDTO = new RecipeRequestDTO();
        recipeRequestDTO.setName("Sweet Potato and Red Onion");
        recipeRequestDTO.setIngredients(ingredientDTOs);
        recipeRequestDTO.setServes(4);
        recipeRequestDTO.setInstructions("Roast in the oven for about 20 minutes or until the potatoes and the sweet potatoes are fork tender");
        recipeRequestDTO.setVegetarian(true);

        Recipe updatedRecipe = recipeService.update(recipeRequestDTO, recipe.getId());

        assertEquals(updatedRecipe.getId(), recipe.getId());
        assertNotEquals(updatedRecipe.getServes(), recipe.getServes());
        assertNotEquals(updatedRecipe.getInstructions(), recipe.getInstructions());
        assertNotEquals(updatedRecipe.getInstructions(), recipe.getInstructions());
    }

    @Test
    void searchRecipe_findMatchRecipes_returnRecipes() {
        User user = new User();
        user.setUsername("Jose");
        user.setPassword("123");
        user.setRoles(new HashSet<>());
        user = userRepository.save(user);

        Recipe recipe = new Recipe();
        recipe.setUser(user);
        Ingredient ingredient = new Ingredient();
        ingredient.setName("garlic");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);
        recipe.setServes(2);
        recipe.setVegetarian(true);
        recipe.setInstructions("Roast in the oven for about 20 minutes");
        recipe.setName("Sweet Potato and Red Onion");

        Recipe recipe2 = new Recipe();
        recipe2.setUser(user);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("garlic");
        List<Ingredient> ingredients2 = new ArrayList<>();
        ingredients2.add(ingredient2);
        recipe2.setIngredients(ingredients2);
        recipe2.setServes(4);
        recipe2.setVegetarian(false);
        recipe2.setInstructions("Cook the rice in a large pan of boiling water for 15 minutes or until tender on stove.");
        recipe2.setName("Chicken and broccoli stir-fry recipe");

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe);
        recipeList.add(recipe2);

        recipeRepository.saveAll(recipeList);

        String searchIngredient = "name:garlic";
        String searchRecipe = "serves:4,instructions:stove";

        List<Recipe> recipes = recipeService.retrieve(searchRecipe, searchIngredient, 0, 10);
        assertEquals(recipes.size(), 1);
    }
}
