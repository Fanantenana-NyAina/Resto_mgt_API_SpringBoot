package com.resto_spring_boot.service;

import com.resto_spring_boot.dao.operations.DishDAO;
import com.resto_spring_boot.dao.operations.DishIngredientDAO;
import com.resto_spring_boot.models.dish.Dish;
import com.resto_spring_boot.models.dish.DishIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishDAO dishDAO;
    private final DishIngredientDAO dishIngredientDAO;

    public List<Dish> getAllDish(int page, int size) {
        return dishDAO.getAll(page, size);
    }

    public Dish addIngredients(int idDish, List<DishIngredient> toAddDishIngredients) {
        Dish dish = dishDAO.getById(idDish);

        List<DishIngredient> dishIngredientsToSave = toAddDishIngredients.stream()
                .map(dishIngredient -> {
                    DishIngredient di = new DishIngredient(dish, dishIngredient.getIngredient(), dishIngredient.getRequireQuantity(), dishIngredient.getUnit());
                    return di;
                })
                .toList();


        List<DishIngredient> saved = dishIngredientDAO.saveAll(dishIngredientsToSave);
        dish.getDishIngredients().addAll(saved);

        return dishDAO.getById(idDish);
    }

    public Dish addDishes(int idDish, List<DishIngredient> dishesToAdd) {
        Dish dish = dishDAO.getById(idDish);
        dish.addIngredients(dishesToAdd);

        List<Dish> dishesSaved = dishDAO.saveAll(List.of(dish));
        return dishesSaved.getFirst();
    }
}
