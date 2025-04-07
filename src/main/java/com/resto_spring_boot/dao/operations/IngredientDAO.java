package com.resto_spring_boot.dao.operations;

import com.resto_spring_boot.models.Ingredient.Ingredient;
import com.resto_spring_boot.models.Unit;
import com.resto_spring_boot.dao.DbConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientDAO implements DAO<Ingredient> {
    private final DbConnection dataSource = new DbConnection();

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
                    int id = rs.getInt("id_ingredient");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    Unit unit = Unit.valueOf(rs.getString("unit"));
                    LocalDateTime updateDateTime = rs.getTimestamp("history_date").toLocalDateTime();

                    ingredient = new Ingredient(id, name, price, unit, updateDateTime);
                    ingredients.add(ingredient);
                }

                return ingredients;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ingredient getById(int id) {
        Ingredient ingredient = null;

        String sql = "select i.id_ingredient, i.name, i.unit, iph.price, iph.history_date from ingredient i " +
                "join ingredient_price_history iph on i.id_ingredient = iph.id_ingredient where i.id_ingredient = ?";

        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idIngredient = rs.getInt("id_ingredient");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    Unit unit = Unit.valueOf(rs.getString("unit"));
                    LocalDateTime updateDateTime = rs.getTimestamp("history_date").toLocalDateTime();

                    ingredient = new Ingredient(idIngredient, name, price, unit, updateDateTime);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ingredient;
    }

    @Override
    public List<Ingredient> saveAll(List<Ingredient> entities) {
        List<Ingredient> newIngredients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            entities.forEach(entityToSave -> {
                try (PreparedStatement ps = connection.prepareStatement("insert into ingredient (id_ingredient, name, unit) values (?, ?, ?)")) {
                    ps.setInt(1, entityToSave.getIdIngredient());
                    ps.setString(2, entityToSave.getIngredientName());
                    ps.setObject(3, entityToSave.getUnit());

                    ps.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                newIngredients.add(getById(entityToSave.getIdIngredient()));
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return newIngredients;
    }
}
