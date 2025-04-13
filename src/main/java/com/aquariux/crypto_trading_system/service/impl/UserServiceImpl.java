package com.aquariux.crypto_trading_system.service.impl;

import com.aquariux.crypto_trading_system.model.entity.User;
import com.aquariux.crypto_trading_system.respository.UserRepository;
import com.aquariux.crypto_trading_system.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }
}
