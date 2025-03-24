-- User Table: Insert test data
INSERT INTO "User" (id, username, password, email, role, version, created_at, updated_at)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'alice', 'password123', 'alice@example.com', 'USER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440001', 'bob', 'securePass', 'bob@example.com', 'USER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440002', 'charlie', 'p@ssw0rd', 'charlie@example.com', 'ADMIN', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Asset Table: Insert test data for users' wallets (USDT, ETH, BTC only)
INSERT INTO Asset (user_id, crypto_type, balance, version)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'BTC', 1.5, 1),
  ('550e8400-e29b-41d4-a716-446655440001', 'ETH', 10.0, 1),
  ('550e8400-e29b-41d4-a716-446655440002', 'USDT', 5.0, 1);

-- Trade Table: Insert test trade transactions for ETH and BTC only
INSERT INTO Trade (user_id, crypto_pair, trade_type, trade_amount, trade_price, trade_timestamp)
VALUES
('550e8400-e29b-41d4-a716-446655440001', 'BTCUSD', 'BUY', 0.5, 40000.12, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440001', 'ETHUSD', 'SELL', 1.2, 2500.50, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440001', 'BNBUSD', 'BUY', 3.0, 350.75, CURRENT_TIMESTAMP);

ALTER TABLE Trade ALTER COLUMN id SET DEFAULT RANDOM_UUID();


-- Price Table: Insert test price data for ETH/USDT and BTC/USDT
INSERT INTO Price (crypto_pair, bid_price, ask_price, created_at)
VALUES
  ('BTCUSD', 40000.12345678, 40010.87654321, CURRENT_TIMESTAMP),
  ('ETHUSD', 2500.12345678, 2510.87654321, CURRENT_TIMESTAMP),
  ('BNBUSD', 350.12345678, 351.87654321, CURRENT_TIMESTAMP);

--------------------
INSERT INTO "User" (id, username, password, email, role, version, created_at, updated_at)
VALUES
  ('550e8400-e29b-41d4-a716-446655440000', 'alice', 'password123', 'alice@example.com', 'USER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440001', 'bob', 'securePass', 'bob@example.com', 'USER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440002', 'charlie', 'p@ssw0rd', 'charlie@example.com', 'ADMIN', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440003', 'david', 'pass123', 'david@example.com', 'USER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440004', 'emma', 'pass456', 'emma@example.com', 'USER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440005', 'frank', 'pass789', 'frank@example.com', 'USER', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('550e8400-e29b-41d4-a716-446655440006', 'grace', 'gracepass', 'grace@example.com', 'ADMIN', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
