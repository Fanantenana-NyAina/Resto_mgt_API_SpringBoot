package com.resto_spring_boot.dao.operations.order;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.DishOrderStatusHistoryMapper;
import com.resto_spring_boot.dao.operations.DAO;
import com.resto_spring_boot.models.order.DishOrderStatusHistory;
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
public class DishOrderStatusHistoryDAO implements DAO<DishOrderStatusHistory> {
    private final DishOrderStatusHistoryMapper dishOrderStatusHistoryMapper;
    private final DbConnection dataSource;
    @Override
    public List<DishOrderStatusHistory> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public DishOrderStatusHistory getById(int id) {
        return null;
    }

    @Override
    public List<DishOrderStatusHistory> saveAll(List<DishOrderStatusHistory> entity) {
        return List.of();
    }

    public List<DishOrderStatusHistory> findDishOrderStatusByDishAndOrder(int idDish, int idOrderRef) {
        String sql = """
        select\s
            dio.id_dish,
            dio.id_order_as_reference,
            dos.dish_order_status,
            dos.status_datetime
        from dish_order_status dos
        join dish_in_order dio\s
            on dos.id_dish_in_order = dio.id_dish_in_order
        where dio.id_dish = ? and dio.id_order_as_reference = ?
   \s""";

        List<DishOrderStatusHistory> statusList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idDish);
            ps.setInt(2, idOrderRef);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    statusList.add(dishOrderStatusHistoryMapper.apply(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return statusList;
    }

}
