package com.aquariux.crypto_trading_system;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "15s")
@EnableCaching
@SpringBootApplication
public class CryptoTradingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTradingSystemApplication.class, args);
	}

}
