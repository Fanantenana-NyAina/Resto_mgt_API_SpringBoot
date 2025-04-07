package com.resto_spring_boot.service;

import com.resto_spring_boot.models.Ingredient.Ingredient;
import com.resto_spring_boot.dao.operations.IngredientDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientDAO dao;

    public IngredientService(IngredientDAO dao) {
        this.dao = dao;
    }

    public List<Ingredient> getAllIngredients() {
        return dao.getAll();
    }

    public Ingredient getIngredientById(int id) {
        return dao.getById(id);
    }

    public void saveIngredients(List<Ingredient> ingredients) {
        try {
            dao.saveAll(ingredients);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateIngredientById(Ingredient ingredient) {
        int rows = dao.update(ingredient);
        return rows > 0;
    }
}
