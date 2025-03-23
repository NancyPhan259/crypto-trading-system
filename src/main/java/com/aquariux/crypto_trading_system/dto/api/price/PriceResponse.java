package com.aquariux.crypto_trading_system.dto.api.price;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceResponse {
    private String cryptoPair;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private ZonedDateTime timestamp;

    @JsonCreator
    public PriceResponse(@JsonProperty("cryptoPair") String cryptoPair,
                 @JsonProperty("bidPrice") BigDecimal bidPrice,
                 @JsonProperty("askPrice") BigDecimal askPrice) {
        this.cryptoPair = cryptoPair;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setCryptoPair(String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
