package com.aquariux.crypto_trading_system;

import com.aquariux.crypto_trading_system.config.TestSecurityConfig;
import com.aquariux.crypto_trading_system.model.TradeSide;
import com.aquariux.crypto_trading_system.model.entity.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
public class TradeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    public void testExecuteTrade() {
        // Sample data
        Map<String, Object> payload = Map.of(
                "userId", "user1",
                "cryptoPair", "BTCUSDT",
                "quantity", 0.1,
                "side", TradeSide.BUY
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Trade> response = restTemplate.postForEntity(getBaseUrl("/api/trade/execute"), request, Trade.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCryptoPair()).isEqualTo("BTCUSDT");
    }

    @Test
    public void testGetTradeHistory() {
        ResponseEntity<Trade[]> response = restTemplate.getForEntity(getBaseUrl("/api/trade/history/user1"), Trade[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}

