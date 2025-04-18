package com.resto_spring_boot.dao.operations.order;

import com.resto_spring_boot.dao.DbConnection;
import com.resto_spring_boot.dao.mapper.OrderStatusHistoryMapper;
import com.resto_spring_boot.dao.operations.DAO;
import com.resto_spring_boot.models.order.OrderStatusHistory;
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
public class OrderStatusHistoryDAO implements DAO<OrderStatusHistory> {
    private final DbConnection dataSource;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Override
    public List<OrderStatusHistory> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public OrderStatusHistory getById(int id) {
        return null;
    }

    @Override
    public List<OrderStatusHistory> saveAll(List<OrderStatusHistory> entity) {
        return List.of();
    }

    public List<OrderStatusHistory> findByOrderRef(int OrderRef) {
        List<OrderStatusHistory> orderStatusHistoriesList = new ArrayList<>();
        String sql = "select osh.id_order_status_history, osh.order_status, osh.status_datetime " +
                "from order_status_history osh join \"order\" o on osh.id_order_as_reference = o.id_order_as_reference " +
                "where osh.id_order_as_reference = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, OrderRef);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderStatusHistory orderStatus = orderStatusHistoryMapper.apply(rs);
                    orderStatusHistoriesList.add(orderStatus);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderStatusHistoriesList;
    }

}
