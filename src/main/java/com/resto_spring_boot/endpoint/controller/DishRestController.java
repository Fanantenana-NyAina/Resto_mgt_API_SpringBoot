package com.resto_spring_boot.endpoint.controller;

import com.resto_spring_boot.endpoint.mapper.DishRestMapper;
import com.resto_spring_boot.endpoint.rest.DishRest;
import com.resto_spring_boot.models.dish.Dish;
import com.resto_spring_boot.service.DishService;
import com.resto_spring_boot.service.exception.ClientException;
import com.resto_spring_boot.service.exception.NotFoundException;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController()
@RequiredArgsConstructor
public class DishRestController {
    private final DishService dishService;
    private final DishRestMapper dishRestMapper;

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
}
