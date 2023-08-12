create table accounts(
    account_id VARCHAR(10) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE portfolio (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    account_id VARCHAR(10) NOT NULL,
    symbol VARCHAR(10),
    CONSTRAINT uc_portfolio_symbol_account UNIQUE (symbol, account_id)
);


CREATE TABLE trades (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    portfolio_id INT NOT NULL,
    account_id VARCHAR(10) NOT NULL, 
    username VARCHAR(50) NOT NULL,
    exchange VARCHAR(10),
    symbol VARCHAR(10),
    stock_name VARCHAR(50),
    units NUMERIC(15,2),
    buy_date DATE,
    buy_price NUMERIC(8,2),
    currency VARCHAR(10),
    total NUMERIC(15,2),
    FOREIGN KEY (portfolio_id) REFERENCES portfolio(id) ON DELETE CASCADE
);