package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.dao.mapper.IngredientMapper;
import com.resto_spring_boot.models.Ingredient.Ingredient;
import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.service.exception.NotFoundException;
import com.resto_spring_boot.service.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientDAO implements DAO<Ingredient> {
    private final DbConnection dataSource = new DbConnection();
    private final IngredientMapper ingredientMapper;
    private final IngredientPriceHistoryDAO ingredientPriceHistoryDAO;
    private final StockMovementDAO stockMovementDAO;

    @Override
    public List<Ingredient> getAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "select i.id_ingredient, i.name, i.unit from ingredient i order by i.id_ingredient asc offset ? limit ?";

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            try (ResultSet rs = ps.executeQuery()) {
                Ingredient ingredient = null;
                while (rs.next()) {
                    ingredient = ingredientMapper.apply(rs);
                    ingredients.add(ingredient);
                }

                return ingredients;
            }

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public Ingredient getById(int id) {
        String sql = "select i.id_ingredient, i.name, di.required_quantity, di.unit from ingredient i"
                + " join dish_ingredient di on i.id_ingredient = di.id_ingredient"
                + " where i.id_ingredient = ?";

        try ( Connection con = dataSource.getConnection();
              PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return ingredientMapper.apply(rs);
                }

                throw new NotFoundException("Ingredient.id_ingredient =" + id + " not found");
            }

        } catch (SQLException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public List<Ingredient> saveAll(List<Ingredient> entities) {
        List<Ingredient> newIngredients = new ArrayList<>();
        String sql = "insert into ingredient (id_ingredient, name) values (?, ?)"
                + " on conflict (id_ingredient) do update set name=excluded.name"
                + " returning id_ingredient, name";

        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                entities.forEach(entityToSave -> {
                    try {
                        ps.setInt(1, entityToSave.getIdIngredient());
                        ps.setString(2, entityToSave.getIngredientName());
                        ps.addBatch();
                    } catch (SQLException e) {
                        throw new ServerException(e);
                    }
                    ingredientPriceHistoryDAO.saveAll((entityToSave.getPrices()));
                    stockMovementDAO.saveAll((entityToSave.getStockMovements()));
                });
                try(ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        newIngredients.add(ingredientMapper.apply(rs));
                    }
                }
            }

        } catch (SQLException e) {
            throw new ServerException(e);
        }

        return newIngredients;
    }
}
