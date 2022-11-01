package com.abn.service.impl;

import com.abn.dto.RecipeRequestDTO;
import com.abn.dto.SearchCriteria;
import com.abn.entity.Ingredient;
import com.abn.entity.Recipe;
import com.abn.entity.User;
import com.abn.exception.CustomException;
import com.abn.mapper.RecipeRequestMapper;
import com.abn.repository.RecipeRepository;
import com.abn.repository.UserRepository;
import com.abn.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * @author Mostafa
 * @version 1.0
 * @since 2022-11-01
 */

@Slf4j
@AllArgsConstructor
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeRequestMapper mapper;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method creates new recipe for user who is logged in
     *
     * @param recipeDTO the required fields of recipe
     * @param username  user who logged in to the system
     * @return the created recipe
     * @see Recipe
     */
    @Transactional
    @Override
    public Recipe create(RecipeRequestDTO recipeDTO, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("user not found", HttpStatus.NOT_FOUND);
        }
        Recipe recipe = mapper.toEntity(recipeDTO);
        recipe.setUser(user);
        return recipeRepository.save(recipe);
    }

    /**
     * This method delete a recipe by id
     *
     * @param id this is recipe id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("request to delete recipe by id {}", id);
        if (!recipeRepository.existsById(id)) {
            throw new CustomException("recipe not found", HttpStatus.NOT_FOUND);
        }
        recipeRepository.deleteById(id);
        log.info("recipe successfully deleted fo the id {}", id);
    }


    /**
     * This method always return a Recipe if it's exist already return updated recipe
     * if recipe does not exit return new one.
     *
     * @param recipeDTO the recipe fields
     * @param id        recipe id
     * @return recipe as a result
     */
    @Override
    @Transactional
    public Recipe update(RecipeRequestDTO recipeDTO, Long id) {
        log.info("request to update recipe by id {}", id);
        Recipe recipe = mapper.toEntity(recipeDTO);
        return recipeRepository.findById(id).map(r -> {
                    recipe.setUser(r.getUser());
                    recipe.setId(r.getId());
                    return recipeRepository.save(recipe);
                })
                .orElseGet(() -> {
                    UserDetails principal = (UserDetails) SecurityContextHolder
                            .getContext()
                            .getAuthentication().getPrincipal();
                    User user = userRepository.findByUsername(principal.getUsername());
                    recipe.setUser(user);
                    recipe.setId(id);
                    return recipeRepository.save(recipe);
                });
    }

    /**
     * This method always returns Recipes, whether the
     * Recipes exist or not.
     *
     * @param searchRecipe     recipe filed in search filter
     * @param searchIngredient Ingredient field in search filter
     * @param page             page number
     * @param size             page size
     * @return list of recipes
     * @see List<Recipe>
     */
    @Override
    @Transactional(readOnly = true)
    public List<Recipe> retrieve(String searchRecipe, String searchIngredient, int page, int size) {
        List<SearchCriteria> paramsRecipe = setSearchCriteria(searchRecipe);
        List<SearchCriteria> paramsIngredient = setSearchCriteria(searchIngredient);
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> recipePage = search(paramsRecipe, paramsIngredient, pageable);
        return recipePage.getContent();
    }

    public Page<Recipe> search(List<SearchCriteria> paramsRecipe, List<SearchCriteria> paramsIngredient, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteriaQuery = criteriaBuilder.createQuery(Recipe.class);
        Root<Recipe> Recipe = criteriaQuery.from(Recipe.class);
        Join<Recipe, Ingredient> ingredients = Recipe.join("ingredients", JoinType.INNER);
        Predicate predicateIngredient;
        Predicate finalPredicate;
        Predicate predicateRecipe = predicateRootBuilder(criteriaBuilder, paramsRecipe, Recipe);

        if (paramsIngredient.isEmpty()) {
            finalPredicate = predicateRecipe;
        } else {
            predicateIngredient = predicateJoinBuilder(criteriaBuilder, paramsIngredient, ingredients);
            finalPredicate = criteriaBuilder.and(predicateRecipe, predicateIngredient);
        }
        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();

        if (sort.isSorted()) {
            criteriaQuery.orderBy(toOrders(sort, Recipe, criteriaBuilder));
        }

        criteriaQuery.distinct(true);

        criteriaQuery.where(finalPredicate);

        TypedQuery<Recipe> typedQuery = entityManager.createQuery(criteriaQuery);

        long total = typedQuery.getResultList().size();

        // Sets the offset position in the result set to start pagination
        typedQuery.setFirstResult((int) pageable.getOffset());

        // Sets the maximum number of entities that should be included in the page
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Recipe> content = total > pageable.getOffset() ? typedQuery.getResultList() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    public Predicate predicateRootBuilder(CriteriaBuilder builder, List<SearchCriteria> params, Root<Recipe> root) {
        Predicate predicate = builder.conjunction();

        for (SearchCriteria param : params) {
            switch (param.getOperation().toLowerCase()) {
                case ">":
                    predicate = builder.and(predicate,
                            builder.greaterThan(root.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case ">:":
                    predicate = builder.and(predicate,
                            builder.greaterThanOrEqualTo(root.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case "<":
                    predicate = builder.and(predicate,
                            builder.lessThan(root.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case "<:":
                    predicate = builder.and(predicate,
                            builder.lessThanOrEqualTo(root.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case ":":
                    if (root.get(param.getKey()).getJavaType() == String.class) {
                        predicate = builder.and(predicate,
                                builder.like(root.get(param.getKey()),
                                        "%" + param.getValue() + "%"));
                    } else if (root.get(param.getKey()).getJavaType() == Boolean.class) {
                        if (param.getValue().equals("true")) {
                            predicate = builder.and(predicate,
                                    builder.equal(root.get(param.getKey()), true));
                        } else {
                            predicate = builder.and(predicate,
                                    builder.equal(root.get(param.getKey()), false));
                        }
                    } else {
                        predicate = builder.and(predicate,
                                builder.equal(root.get(param.getKey()), param.getValue()));
                    }
                    break;
                case "!:":
                    if (root.get(param.getKey()).getJavaType() == String.class) {
                        predicate = builder.and(predicate,
                                builder.notLike(root.get(param.getKey()),
                                        "%" + param.getValue() + "%"));
                    } else {
                        predicate = builder.and(predicate,
                                builder.notEqual(root.get(param.getKey()), param.getValue()));
                    }
                    break;
            }
        }
        return predicate;
    }

    public Predicate predicateJoinBuilder(CriteriaBuilder builder, List<SearchCriteria> params, Join<Recipe, Ingredient> ingredients) {
        Predicate predicate = builder.conjunction();

        for (SearchCriteria param : params) {
            switch (param.getOperation().toLowerCase()) {
                case ">":
                    predicate = builder.and(predicate,
                            builder.greaterThan(ingredients.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case ">:":
                    predicate = builder.and(predicate,
                            builder.greaterThanOrEqualTo(ingredients.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case "<":
                    predicate = builder.and(predicate,
                            builder.lessThan(ingredients.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case "<:":
                    predicate = builder.and(predicate,
                            builder.lessThanOrEqualTo(ingredients.get(param.getKey()),
                                    param.getValue().toString()));
                    break;
                case ":":
                    if (ingredients.get(param.getKey()).getJavaType() == String.class) {
                        predicate = builder.and(predicate,
                                builder.like(ingredients.get(param.getKey()),
                                        "%" + param.getValue() + "%"));
                    } else if (ingredients.get(param.getKey()).getJavaType() == Boolean.class) {
                        if (param.getValue().equals("true")) {
                            predicate = builder.and(predicate,
                                    builder.equal(ingredients.get(param.getKey()), true));
                        } else {
                            predicate = builder.and(predicate,
                                    builder.equal(ingredients.get(param.getKey()), false));
                        }
                    } else {
                        predicate = builder.and(predicate,
                                builder.equal(ingredients.get(param.getKey()), param.getValue()));
                    }
                    break;
                case "!:":
                    if (ingredients.get(param.getKey()).getJavaType() == String.class) {
                        predicate = builder.and(predicate,
                                builder.notLike(ingredients.get(param.getKey()),
                                        "%" + param.getValue() + "%"));
                    } else {
                        predicate = builder.and(predicate,
                                builder.notEqual(ingredients.get(param.getKey()), param.getValue()));
                    }
                    break;
            }
        }
        return predicate;
    }

    /**
     * this method parses the search filter for finding criteria
     * (: !: < <= > >= )
     *
     * @param search this search filter query
     * @return list of Search criteria
     * @see SearchCriteria
     */
    private List<SearchCriteria> setSearchCriteria(String search) {
        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|!:|<|<=|>|>=)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1),
                        matcher.group(2), matcher.group(3)));
            }
        }
        if (StringUtils.isNotBlank(search) && params.isEmpty()) {
            throw new CustomException("bad request in filter query. prevent sql injection", HttpStatus.BAD_REQUEST);
        }
        return params;
    }
}
