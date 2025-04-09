package com.resto_spring_boot.endpoint.controller;

import com.resto_spring_boot.endpoint.mapper.IngredientRestMapper;
import com.resto_spring_boot.endpoint.rest.IngredientRest;
import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.service.IngredientService;
import com.resto_spring_boot.service.exception.ClientException;
import com.resto_spring_boot.service.exception.NotFoundException;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequiredArgsConstructor
public class IngredientRestController {
    private final IngredientService ingredientService;
    private final IngredientRestMapper ingredientRestMapper;

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
                                                 @RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
        try {
            List<Ingredient> retrieveIngredientByPrices = ingredientService.getIngredientsByPrices(priceMinFilter, priceMaxFilter);
            List<IngredientRest> ingredientRests = retrieveIngredientByPrices.stream()
                    .map(ingredient -> ingredientRestMapper.IngredientToIngredientRest(ingredient))
                    .toList();

            return ResponseEntity.ok().body(ingredientRests);

        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredientById(@PathVariable int id) {
        Optional<Ingredient> optionalIngredient = Optional.ofNullable(ingredientService.getIngredientById(id));

        if (optionalIngredient.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalIngredient.get());
        }
        return ResponseEntity.status(NOT_FOUND).body("Ingredient=" + id + " not found");
    }

    @PostMapping("/ingredients/saveAll")
    public ResponseEntity<Object> saveIngredients(@RequestBody List<Ingredient> ingredients) {
        ingredientService.saveIngredients(ingredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredients);
    }

    @PutMapping("/ingredients/{id}/prices")
    public ResponseEntity<Object> updateIngredientPrice(@PathVariable int id, @RequestBody List<Ingredient> ingredients) {
        throw new UnsupportedOperationException("Not suppported yet");
    }

    @PutMapping("/ingredients/{id}/stockMovements")
    public ResponseEntity<Object> updateStockMovement(@PathVariable int id, @RequestBody List<Ingredient> ingredients) {
        throw new UnsupportedOperationException("Not suppported yet");
    }
}
