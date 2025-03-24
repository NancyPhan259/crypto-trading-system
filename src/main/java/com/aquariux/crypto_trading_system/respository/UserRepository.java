package com.aquariux.crypto_trading_system.respository;

import com.aquariux.crypto_trading_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
