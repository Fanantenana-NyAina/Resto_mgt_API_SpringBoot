package com.resto_spring_boot.service;

import com.resto_spring_boot.dao.operations.IngredientPriceHistoryDAO;
import com.resto_spring_boot.dao.operations.StockMovementDAO;
import com.resto_spring_boot.models.ingredient.Ingredient;
import com.resto_spring_boot.dao.operations.IngredientDAO;
import com.resto_spring_boot.models.ingredient.IngredientPriceHistory;
import com.resto_spring_boot.models.stock.StockMovement;
import com.resto_spring_boot.service.exception.ClientException;
import com.resto_spring_boot.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientDAO ingredientDAO;
    private final IngredientPriceHistoryDAO ingredientPriceHistoryDAO;
    private final StockMovementDAO stockMovementDAO;

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

    public Ingredient addPriceHistories(int ingredientId, List<IngredientPriceHistory> toAddIngredientPriceHistories) {
        Ingredient ingredient = ingredientDAO.getById(ingredientId);

        List<IngredientPriceHistory> ingredientPriceHistoriesWithIngredient = toAddIngredientPriceHistories.stream()
                .map(ingredientPriceHisto -> {
                    IngredientPriceHistory ingredientPriceHistory = new IngredientPriceHistory(ingredient, ingredientPriceHisto.getIngredientPrice(), ingredientPriceHisto.getDateTime());
                    return ingredientPriceHistory;
                })
                .toList();

        List<IngredientPriceHistory> savingIngredientPriceHistories = ingredientPriceHistoryDAO.saveAll(ingredientPriceHistoriesWithIngredient);

        ingredient.getPrices().addAll(savingIngredientPriceHistories);
        return ingredientDAO.getById(ingredientId);
    }

    public Ingredient addStockMovement(int ingredientId, List<StockMovement> toAddStockMovement) {
        Ingredient ingredient = ingredientDAO.getById(ingredientId);

        List<StockMovement> ingredientWithStockMovement = toAddStockMovement.stream()
                .map(stockMovement -> {
                    StockMovement stockMov = new StockMovement(ingredient, stockMovement.getQuantity(), stockMovement.getUnit(), stockMovement.getMovementType(), stockMovement.getMovementDateTime());
                    return stockMov;
                })
                .toList();

        List<StockMovement> savingStockMoves = stockMovementDAO.saveAll(ingredientWithStockMovement);

        ingredient.getStockMovements().addAll(savingStockMoves);
        return ingredientDAO.getById(ingredientId);
    }

    public Ingredient findByName(String name) {
        return ingredientDAO.findByName(name);
    }
}
