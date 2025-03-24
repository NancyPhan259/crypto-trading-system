package com.aquariux.crypto_trading_system.dto.api.page;

public class PagingRequest {

    private final String order;
    private final String sort;
    private final Integer pageSize;
    private final String lastId;

    public PagingRequest(String order, String sort, Integer pageSize, String lastId) {
        this.order = order;
        this.sort = sort;
        this.pageSize = pageSize;
        this.lastId = lastId;
    }

    public String getOrder() {
        return order;
    }

    public String getSort() {
        return sort;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getLastId() {
        return lastId;
    }
}
