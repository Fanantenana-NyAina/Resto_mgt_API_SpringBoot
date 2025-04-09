package com.resto_spring_boot.service;

import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.dao.operations.IngredientDAO;
import com.resto_spring_boot.service.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientDAO ingredientDAO;

    public List<Ingredient> getAllIngredients(int page, int size) {
        return ingredientDAO.getAll(page, size);
    }

    public List<Ingredient> getIngredientsByPrices(Double priceMinFilter, Double priceMaxFilter) {
        if (priceMinFilter != null && priceMinFilter < 0) {
            throw new ClientException("PriceMinFilter " + priceMinFilter + " is negative");
        }
        if (priceMaxFilter != null && priceMaxFilter < 0) {
            throw new ClientException("PriceMaxFilter " + priceMaxFilter + " is negative");
        }
        if (priceMinFilter != null && priceMaxFilter != null) {
            if (priceMinFilter > priceMaxFilter) {
                throw new ClientException("PriceMinFilter " + priceMinFilter + " is greater than PriceMaxFilter " + priceMaxFilter);
            }
        }

        List<Ingredient> ingredients = ingredientDAO.getAll(1, 100);
        List<Ingredient> filteredIngredients = ingredients.stream()
                .filter(ingredient -> {
                    if (priceMinFilter == null && priceMaxFilter == null) {
                        return true;
                    }
                    double unitPrice = ingredient.getActualPrice();
                    if (priceMinFilter != null && priceMaxFilter == null) {
                        return unitPrice >= priceMinFilter;
                    }
                    if (priceMinFilter == null) {
                        return unitPrice <= priceMaxFilter;
                    }
                    return unitPrice >= priceMinFilter && unitPrice <= priceMaxFilter;
                })
                .toList();

        return filteredIngredients;
    }

    public Ingredient getIngredientById(int id) {
        return ingredientDAO.getById(id);
    }

    public void saveIngredients(List<Ingredient> ingredients) {
        try {
            ingredientDAO.saveAll(ingredients);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
