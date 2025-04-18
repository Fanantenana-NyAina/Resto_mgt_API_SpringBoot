package com.resto_spring_boot.dao.operations.order;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.DishOrderMapper;
import com.resto_spring_boot.dao.operations.DAO;
import com.resto_spring_boot.models.order.DishOrder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderDAO implements DAO<DishOrder> {
    private final DbConnection dataSource;
    private final DishOrderMapper dishOrderMapper;

    @Override
    public List<DishOrder> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public DishOrder getById(int id) {
        return null;
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> entity) {
        return List.of();
    }

    @SneakyThrows
    public List<DishOrder> findByOrderRef(int orderRef) {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "select dio.id_dish, dio.id_order_as_reference, dio.quantity " +
                "from dish_in_order dio " +
                "join dish d on d.id_dish = dio.id_dish " +
                "join \"order\" o on dio.id_order_as_reference = o.id_order_as_reference " +
                "where dio.id_order_as_reference = ?";


        try(Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, orderRef);
            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    DishOrder dishOrder = dishOrderMapper.apply(rs);
                    dishOrders.add(dishOrder);
                }
                return dishOrders;
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
