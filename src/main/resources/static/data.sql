--- USER ----
INSERT INTO APP_USER (id, username) VALUES ('user1', 'Alice');
INSERT INTO APP_USER (id, username) VALUES ('user2', 'Bob');

-- ASSET ----
INSERT INTO asset (user_id, symbol, balance) VALUES ('user1', 'BTCUSDT', 1.5);
INSERT INTO asset (user_id, symbol, balance) VALUES ('user1', 'ETHUSDT', 5.0);

-- TRADE ----
INSERT INTO trade (id, user_id, crypto_pair, price, amount, timestamp)
VALUES (RANDOM_UUID(), 'user1', 'BTCUSDT', 60000.0, 0.5, CURRENT_TIMESTAMP());