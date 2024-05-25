-- Inserción de tarjetas en la tabla Card
INSERT INTO Card (cardId, productName, holderName, expirationDate, isActive, isBlocked, balance) 
VALUES ('123456789', 'Tarjeta de Crédito', 'Juan Perez', '2025-12-31', true, false, 1000.00);

INSERT INTO Card (cardId, productName, holderName, expirationDate, isActive, isBlocked, balance) 
VALUES ('987654321', 'Tarjeta de Débito', 'María Gomez', '2026-11-30', true, false, 500.00);

-- Inserción de transacciones en la tabla Transaction
INSERT INTO Transaction (transactionId, card_id, amount, timestamp, isCancelled) 
VALUES (1, '123456789', 100.00, '2024-05-24T10:00:00', false);

INSERT INTO Transaction (transactionId, card_id, amount, timestamp, isCancelled) 
VALUES (2, '123456789', 200.00, '2024-05-24T11:00:00', false);

INSERT INTO Transaction (transactionId, card_id, amount, timestamp, isCancelled) 
VALUES (3, '987654321', 50.00, '2024-05-24T12:00:00', false);

INSERT INTO Transaction (transactionId, card_id, amount, timestamp, isCancelled) 
VALUES (4, '987654321', 75.00, '2024-05-24T13:00:00', true);
