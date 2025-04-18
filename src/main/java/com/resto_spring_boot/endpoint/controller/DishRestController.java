package com.resto_spring_boot.endpoint.controller;

import com.resto_spring_boot.endpoint.mapper.DishRestMapper;
import com.resto_spring_boot.endpoint.rest.CreateDishIngredients;
import com.resto_spring_boot.endpoint.rest.CreateStockMovement;
import com.resto_spring_boot.endpoint.rest.DishRest;
import com.resto_spring_boot.models.dish.Dish;
import com.resto_spring_boot.models.dish.DishIngredient;
import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.models.stock.StockMovement;
import com.resto_spring_boot.service.DishService;
import com.resto_spring_boot.service.IngredientService;
import com.resto_spring_boot.service.exception.ClientException;
import com.resto_spring_boot.service.exception.NotFoundException;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequiredArgsConstructor
public class DishRestController {
    private final DishService dishService;
    private final DishRestMapper dishRestMapper;
    private final IngredientService ingredientService;

    @GetMapping("/dishes")
    public ResponseEntity<Object> getDishes(@RequestParam(name="page", required = false) int page,
                                            @RequestParam(name="size", required = false) int size) {

        try {
            List<Dish> paginateDishes = dishService.getAllDish(page, size);
            List<DishRest> dishesRest = paginateDishes.stream()
                    .map(dish -> dishRestMapper.DishToDishRest(dish))
                    .toList();

            return ResponseEntity.ok().body(dishesRest);
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<Object> updateIngredients(@PathVariable int id,
                                                    @RequestBody List<CreateDishIngredients> dishIngredients){
        List<DishIngredient> ingredients = dishIngredients.stream()
                .map(ingredient->
                        new DishIngredient(
                                new Ingredient(ingredient.getIngredient().getIdIngredient(),ingredient.getIngredient().getIngredientName()),
                                ingredient.getRequiredQuantity(),
                                ingredient.getUnit())
                ).toList();
        Dish ingredient = dishService.addDishes(id,ingredients);
        return ResponseEntity.status(HttpStatus.OK).body(ingredient);
    }

    /*@PutMapping("/dishes/{idDish}/ingredients")
    public ResponseEntity<Object> updateStockMovement(@PathVariable int idDish,
                                                      @RequestBody List<CreateDishIngredients> dishIngredients) {

        List<DishIngredient> stockMoves = dishIngredients.stream()
                .map(dto -> new DishIngredient(dto.getQuantity(), dto.getUnit(), dto.getMovementType(), dto.getMovementDateTime()))
                .toList();

        Ingredient ingredient = ingredientService.addStockMovement(idIngredient, stockMoves);

        return ResponseEntity.ok().body(ingredientRestMapper.IngredientToIngredientRest(ingredient));
    }*/
}
