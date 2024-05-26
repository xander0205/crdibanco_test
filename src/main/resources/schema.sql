CREATE TABLE Card (
    cardId VARCHAR(16) PRIMARY KEY,
    productName VARCHAR(255),
    holderName VARCHAR(255),
    expirationDate DATE,
    isActive BOOLEAN,
    isBlocked BOOLEAN,
    balance DECIMAL(19,2)
);

CREATE TABLE Transaction (
    transactionId BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id VARCHAR(16),
    amount DECIMAL(19,2),
    timestamp TIMESTAMP,
    isCancelled BOOLEAN,
    FOREIGN KEY (card_id) REFERENCES Card(cardId)
);