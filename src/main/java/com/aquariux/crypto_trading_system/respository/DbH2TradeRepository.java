package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.dto.api.page.PagingRequest;
import com.aquariux.crypto_trading_system.entity.Trade;
import com.aquariux.crypto_trading_system.respository.spec.TradeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@Repository
public class DbH2TradeRepository implements TradeRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbH2TradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Trade save(Trade trade) {
        String sql = "INSERT INTO Trade (id, user_id, crypto_pair, trade_type, trade_amount, trade_price) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, trade.getId().toString());
            ps.setString(2, trade.getUserId());
            ps.setString(3, trade.getCryptoPair());
            ps.setString(4, trade.getTradeType());
            ps.setBigDecimal(5, trade.getTradeAmount());
            ps.setBigDecimal(6, trade.getTradePrice());
        });

        return trade;
    }


    @Override
    public int countTrades(String userId) {
        String sql = "SELECT COUNT(*) FROM Trade WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
    }

    @Override
    public List<Trade> getAllTrades(String userId, PagingRequest pageRequest) {
        String baseQuery = "SELECT * FROM Trade ";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        queryBuilder.append("WHERE user_id = ? ");

        String operator = pageRequest.getOrder().equalsIgnoreCase("asc") ? ">" : "<";
        if (pageRequest.getLastId() != null) {
            queryBuilder.append("AND id ").append(operator).append(" ? ");
        }

        queryBuilder.append("ORDER BY ")
                .append(pageRequest.getSort())
                .append(" ")
                .append(pageRequest.getOrder());

        if (!pageRequest.getSort().equals("id")) {
            queryBuilder.append(", id ").append(pageRequest.getOrder());
        }

        queryBuilder.append(" LIMIT ?");

        String query = queryBuilder.toString();

        List<Object> params = new LinkedList<>();
        params.add(userId);

        if (pageRequest.getLastId() != null) {
            params.add(pageRequest.getLastId());
        }

        params.add(pageRequest.getPageSize());

        return jdbcTemplate.query(query, params.toArray(), (rs, rowNum) -> new Trade(
                UUID.fromString(rs.getString("id")),
                rs.getString("user_id"),
                rs.getString("crypto_pair"),
                rs.getString("trade_type"),
                rs.getBigDecimal("trade_amount"),
                rs.getBigDecimal("trade_price"),
                rs.getTimestamp("trade_timestamp").toInstant().atZone(ZoneId.systemDefault())
        ));
    }
}
