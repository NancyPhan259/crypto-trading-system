package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.dto.AggregatedPrice;
import com.aquariux.crypto_trading_system.dto.api.page.PagingRequest;
import com.aquariux.crypto_trading_system.entity.Price;
import com.aquariux.crypto_trading_system.entity.Trade;
import com.aquariux.crypto_trading_system.respository.spec.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Repository
public class DbH2PriceRepository implements PriceRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbH2PriceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Price> saveAggregatedPrices(Collection<AggregatedPrice> cryptoPairToAggregatedPrice) {
        String sql = "INSERT INTO Price (crypto_pair, bid_price, ask_price, created_at) " +
                "VALUES (?, ?, ?, ?)";

        List<Price> prices = new LinkedList<>();
        for (AggregatedPrice price : cryptoPairToAggregatedPrice) {

            ZonedDateTime now = ZonedDateTime.now();

            jdbcTemplate.update(sql,
                    price.getSymbol(),
                    price.getBestBid(),
                    price.getBestAsk(),
                    now
            );

            prices.add(new Price(price.getSymbol(), price.getBestBid(), price.getBestAsk(), now));
        }

        return prices;
    }

    @Override
    public Optional<Price> findPrice(String cryptoPair) {
        String sql = "SELECT * FROM Price WHERE crypto_pair = ? ORDER BY timestamp DESC LIMIT 1";

        try {

            Price price = jdbcTemplate.queryForObject(sql, new Object[]{cryptoPair}, (rs, rowNum) -> {
                int id = rs.getInt("id");
                BigDecimal bidPrice = rs.getBigDecimal("bid_price");
                BigDecimal askPrice = rs.getBigDecimal("ask_price");
                ZonedDateTime timestamp = rs.getTimestamp("timestamp").toInstant().atZone(ZoneId.systemDefault());

                return new Price(id, cryptoPair, bidPrice, askPrice, timestamp);
            });

            return Optional.of(price);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

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
