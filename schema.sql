create table accounts(
    account_id VARCHAR(10) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    cash NUMERIC(15,2) DEFAULT 1000000
);

CREATE TABLE portfolio (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    account_id VARCHAR(10) NOT NULL,
    symbol VARCHAR(10),
    units NUMERIC(15,2),
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
    action VARCHAR(10), 
    trade_date DATE,
    price NUMERIC(8,4),
    currency VARCHAR(10),
    total NUMERIC(15,2),
    FOREIGN KEY (portfolio_id) REFERENCES portfolio(id) ON DELETE CASCADE
);