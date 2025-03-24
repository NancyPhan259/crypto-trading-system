package com.aquariux.crypto_trading_system.dto.api.trade;

import com.aquariux.crypto_trading_system.util.validation.CryptoPairValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class TradeRequest {
    @NotBlank(message = "Crypto pair is required")
    @CryptoPairValid
    private String cryptoPair;

    @NotBlank(message = "Trade type is required")
    @Pattern(regexp = "BUY|SELL", message = "Trade type must be either 'BUY' or 'SELL'")
    private String tradeType;

    @Min(value = 0, message = "Trade amount must be greater than 0")
    private BigDecimal tradeAmount;


    public TradeRequest() {
    }

    public void setCryptoPair(@NotBlank(message = "Crypto pair is required") String cryptoPair) {
        this.cryptoPair = cryptoPair;
    }

    public void setTradeType(@NotBlank(message = "Trade type is required") @Pattern(regexp = "BUY|SELL", message = "Trade type must be either 'BUY' or 'SELL'") String tradeType) {
        this.tradeType = tradeType;
    }

    public void setTradeAmount(@Min(value = 0, message = "Trade amount must be greater than 0") BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public TradeRequest(String cryptoPair, String tradeType, BigDecimal tradeAmount) {
        this.cryptoPair = cryptoPair;
        this.tradeType = tradeType;
        this.tradeAmount = tradeAmount;
    }

    public String getCryptoPair() {
        return cryptoPair;
    }

    public String getTradeType() {
        return tradeType;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }
}
