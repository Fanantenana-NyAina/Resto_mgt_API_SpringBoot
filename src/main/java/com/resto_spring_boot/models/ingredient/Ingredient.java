package com.resto_spring_boot.models.ingredient;

import com.resto_spring_boot.models.stock.StockMovement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.resto_spring_boot.models.stock.Movement.IN;
import static com.resto_spring_boot.models.stock.Movement.OUT;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ingredient {
    private int idIngredient;
    private String ingredientName;
    private List<IngredientPriceHistory> prices;
    private List<StockMovement> stockMovements;

    public List<StockMovement> addStockMovements(List<StockMovement> stockMovements) {
        stockMovements.forEach(stockMovement -> stockMovement.setIngredient(this));
        if (getStockMovements() == null || getStockMovements().isEmpty()) {
            return stockMovements;
        }
        getStockMovements().addAll(stockMovements);
        return getStockMovements();
    }

    public List<IngredientPriceHistory> addPrices(List<IngredientPriceHistory> newPrices) {
        if (newPrices == null) return this.prices;

        // Initialisation
        if (this.prices == null) {
            this.prices = new ArrayList<>();
        }

        // Vérification finale
        newPrices.forEach(price -> {
            if (price.getIngredient() == null) {
                throw new IllegalStateException("ERREUR CRITIQUE: Historique sans ingrédient");
            }
        });

        this.prices.addAll(newPrices);
        return this.prices;
    }

    //rectified1
    /*public List<IngredientPriceHistory> addPrices(List<IngredientPriceHistory> newPrices) {
        if (newPrices == null || newPrices.isEmpty()) {
            return getPrices();
        }

        newPrices.forEach(price -> price.setIngredient(this));

        if (this.prices == null) {
            this.prices = new ArrayList<>();
        }

        this.prices.addAll(newPrices);
        return this.prices;
    }


    public Double getActualPrice() {
        return findActualPrice().orElse(
                new IngredientPriceHistory(
                        0,
                        this,
                        0.0, LocalDateTime.now())).getIngredientPrice();
    }

    public Double getAvailableQuantity() {
        return getAvailableQuantityAt(LocalDateTime.now());
    }

    public Double getPriceAt(LocalDateTime dateValue) {
        return findPriceAt(dateValue).orElse(new IngredientPriceHistory(0, this, 0.0, LocalDateTime.now())).getIngredientPrice();
    }

    public Double getAvailableQuantityAt(LocalDateTime datetime) {
        List<StockMovement> stockMovementsBefore = stockMovements.stream()
                .filter(stockMovement ->
                        stockMovement.getMovementDateTime().isBefore(datetime)
                                || stockMovement.getMovementDateTime().isEqual(datetime))
                .toList();

        double quantity = 0;
        for (StockMovement stockMovement : stockMovementsBefore) {
            if (IN.equals(stockMovement.getMovementType())) {
                quantity += stockMovement.getQuantity();
            } else if (OUT.equals(stockMovement.getMovementType())) {
                quantity -= stockMovement.getQuantity();
            }
        }
        return quantity;
    }

    private Optional<IngredientPriceHistory> findPriceAt(LocalDateTime dateValue) {
        return prices.stream()
                .filter(price -> price.getDateTime().toLocalDate().equals(dateValue.toLocalDate()))
                .findFirst();
    }

    private Optional<IngredientPriceHistory> findActualPrice() {
        return prices.stream()
                .max(Comparator.comparing(IngredientPriceHistory::getDateTime));
    }
}
