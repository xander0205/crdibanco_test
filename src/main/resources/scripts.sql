INSERT INTO Card (cardId, productName, holderName, expirationDate, isActive, isBlocked, balance) 
VALUES ('1234567890123456', 'Tarjeta de Crédito', 'Juan Perez', '2027-05-24', true, false, 1000.00);

INSERT INTO Card (cardId, productName, holderName, expirationDate, isActive, isBlocked, balance) 
VALUES ('9876543210123456', 'Tarjeta de Débito', 'María Gomez', '2027-05-24', true, false, 500.00);

INSERT INTO Transaction (card_id, amount, timestamp, isCancelled)
VALUES ('1234567890123456', 200.00, '2024-05-24T22:00:00', false);

INSERT INTO Transaction (card_id, amount, timestamp, isCancelled)
VALUES ('9876543210123456', 100.00, '2024-05-24T22:30:00', false);
