package com.aquariux.crypto_trading_system.service;

import com.aquariux.crypto_trading_system.model.entity.User;

public interface UserService {
    User getUserById(String userId);
}
