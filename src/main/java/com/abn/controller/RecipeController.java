package com.abn.controller;

import com.abn.dto.RecipeRequestDTO;
import com.abn.dto.RecipeResponseDTO;
import com.abn.dto.ResponseMsgDTO;
import com.abn.mapper.RecipeResponseMapper;
import com.abn.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/recipes")
@Api(tags = "Recipe")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeResponseMapper mapper;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<List<RecipeResponseDTO>> search(@RequestParam(value = "searchRecipe", required = false) String searchRecipe,
                                                          @RequestParam(value = "searchIngredient", required = false) String searchIngredient,
                                                          @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "20") Integer size) {

        return ResponseEntity.ok(mapper.toDto(recipeService.retrieve(searchRecipe, searchIngredient, page, size)));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<RecipeResponseDTO> create(@ApiParam("create recipe")
                                                    @Valid @RequestBody RecipeRequestDTO recipeDTO) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(mapper.toDto(recipeService.create(recipeDTO, principal.getUsername())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<RecipeResponseDTO> update(@ApiParam("update recipe")
                                                    @Valid @RequestBody RecipeRequestDTO recipeDTO,
                                                    @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(recipeService.update(recipeDTO, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseMsgDTO> delete(@ApiParam("delete recipe") @PathVariable Long id) {
        recipeService.delete(id);
        return ResponseEntity.ok(ResponseMsgDTO.builder().message("deleted successfully").build());
    }
}
