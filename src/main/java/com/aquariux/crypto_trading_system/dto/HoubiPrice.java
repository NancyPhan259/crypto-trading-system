package com.aquariux.crypto_trading_system.dto;

import java.math.BigDecimal;

public class HoubiPrice {
    private HoubiTicker[] data;

    private String status;

    private long ts;

    public HoubiPrice(HoubiTicker[] data, String status, long ts) {
        this.data = data;
        this.status = status;
        this.ts = ts;
    }

    public HoubiTicker[] getData() {
        return data;
    }

    public static class HoubiTicker {
        private String symbol;
        private BigDecimal bid;
        private BigDecimal bidSize;
        private BigDecimal ask;
        private BigDecimal askSize;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private BigDecimal amount;
        private BigDecimal vol;
        private BigDecimal count;

        private HoubiTicker() {
        }

        public HoubiTicker(String symbol, BigDecimal bid, BigDecimal ask) {
            this.symbol = symbol;
            this.bid = bid;
            this.ask = ask;
        }

        public HoubiTicker(String symbol, BigDecimal bid, BigDecimal bidSize, BigDecimal ask, BigDecimal askSize, BigDecimal open,
                           BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal amount, BigDecimal vol, BigDecimal count) {
            this.symbol = symbol;
            this.bid = bid;
            this.bidSize = bidSize;
            this.ask = ask;
            this.askSize = askSize;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.amount = amount;
            this.vol = vol;
            this.count = count;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public BigDecimal getBid() {
            return bid;
        }

        public void setBid(BigDecimal bid) {
            this.bid = bid;
        }

        public BigDecimal getAsk() {
            return ask;
        }

        public void setAsk(BigDecimal ask) {
            this.ask = ask;
        }
    }
}
