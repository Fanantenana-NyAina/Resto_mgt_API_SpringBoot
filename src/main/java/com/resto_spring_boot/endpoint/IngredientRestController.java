package com.resto_spring_boot.endpoint;

import com.resto_spring_boot.models.Ingredient.Ingredient;
import com.resto_spring_boot.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/")
public class IngredientRestController {
    IngredientService ingredientService;

    public IngredientRestController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String HelloWord() {
        return "Hello World";
    }

    @GetMapping("/ingredients")
    public ResponseEntity<Object> getIngredients(@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
                                                 @RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
        if (priceMinFilter != null && priceMinFilter < 0) {
            return new ResponseEntity<>("PriceMinFilter " + priceMinFilter + " is negative", HttpStatus.BAD_REQUEST);
        }
        if (priceMaxFilter != null && priceMaxFilter < 0) {
            return new ResponseEntity<>("PriceMaxFilter " + priceMaxFilter + " is negative", HttpStatus.BAD_REQUEST);
        }
        if (priceMinFilter != null && priceMaxFilter != null) {
            if (priceMinFilter > priceMaxFilter) {
                return ResponseEntity.badRequest()
                        .body("PriceMinFilter " + priceMinFilter + " is greater than PriceMaxFilter " + priceMaxFilter);
            }
        }

        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        List<Ingredient> filteredIngredients = ingredients.stream()
                .filter(ingredient -> {
                    if (priceMinFilter == null && priceMaxFilter == null) {
                        return true;
                    }
                    double unitPrice = ingredient.getUnitPrice();
                    if (priceMinFilter != null && priceMaxFilter == null) {
                        return unitPrice >= priceMinFilter;
                    }
                    if (priceMinFilter == null) {
                        return unitPrice <= priceMaxFilter;
                    }
                    return unitPrice >= priceMinFilter && unitPrice <= priceMaxFilter;
                })
                .toList();
        return ResponseEntity.ok().body(filteredIngredients);
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredientById(@PathVariable int id) {
        Optional<Ingredient> optionalIngredient = Optional.ofNullable(ingredientService.getIngredientById(id));

        if (optionalIngredient.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalIngredient.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient=" + id + " not found");
    }

    @PostMapping("/ingredients/saveAll")
    public ResponseEntity<Object> saveIngredients(@RequestBody List<Ingredient> ingredients) {
        ingredientService.saveIngredients(ingredients);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredients);
    }

    @PutMapping("/ingredients/update")
    public ResponseEntity<Object> updateIngredient(@RequestBody Ingredient ingredient) {
        ingredientService.updateIngredientById(ingredient);
        return ResponseEntity.status(HttpStatus.OK).body(ingredient);
    }
}
