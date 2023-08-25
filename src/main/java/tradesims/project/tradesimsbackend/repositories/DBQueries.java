package tradesims.project.tradesimsbackend.repositories;

public class DBQueries {
    public static final String INSERT_REGISTRATION = """
        
    insert into accounts(account_id, name, username, email, password)
                      values (?, ?, ?, ? ,?);

  """;

// public static final String SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL = """
//     SELECT t.account_id, t.symbol, t.username, t.exchange, t.stock_name,
//     t.currency, SUM(t.units*t.price)/SUM(t.units) as buy_price, 
//     SUM(t.total) AS total_sum, SUM(t.units) AS total_units 
//     FROM trades AS t
//     RIGHT JOIN portfolio AS p ON t.portfolio_id = p.id
//     WHERE t.account_id = ? AND t.symbol = ? 
//     GROUP BY t.account_id, t.symbol, t.username, t.exchange, t.stock_name, t.currency
//     ORDER BY t.symbol
// """;

public static final String SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL = """
    SELECT t.account_id, t.symbol, t.username, t.exchange, t.stock_name,
    t.currency, SUM(CASE WHEN t.action = "Buy" THEN t.units * t.price ELSE 0 END)/
    SUM(CASE WHEN t.action = "Buy" THEN t.units ELSE 0 END) as buy_price, 
    SUM(t.total) AS total_sum, SUM(t.units) AS total_units 
    FROM trades AS t
    RIGHT JOIN portfolio AS p ON t.portfolio_id = p.id
    WHERE t.account_id = ? AND t.symbol = ? 
    GROUP BY t.account_id, t.symbol, t.username, t.exchange, t.stock_name, t.currency
    ORDER BY t.symbol
""";


  public static final String SELECT_ACCOUNT_BY_EMAIL ="select * from accounts where email = ?";
    
  public static final String CHECK_ACCOUNTID_EXISTS = "SELECT COUNT(*) FROM accounts WHERE account_id = ?";

  public static final String SELECT_SYMBOLS_BY_ACCOUNTID = "SELECT symbol FROM portfolio WHERE account_id = ?";

  public static final String SELECT_TRADES_BY_ACCOUNTID = "SELECT * FROM trades WHERE account_id = ? ORDER BY trade_date DESC";

  public static final String SELECT_UNITS_LEFT_USING_ACCOUNTID_AND_SYMBOL = "SELECT units WHERE account_id = ? AND symbol = ?";

  public static final String DELETE_ROW_FROM_PORTFOLIO_BY_ACCOUNTID = "DELETE FROM portfolio WHERE account_id = ? AND symbol = ?";

  public static final String SELECT_TOTAL_UNITS_BY_ACCOUNTID_AND_SYMBOL = "SELECT units FROM portfolio WHERE account_id = ? AND symbol = ?";

  public static final String INSERT_UPDATE_PORTFOLIO = "insert into portfolio(account_id, symbol, units) values (?, ?, ?) on duplicate key update units = units + ?";


  public static final String SELECT_ACCOUNT_BALANCE ="SELECT cash FROM accounts WHERE account_id = ?";

  public static final String UPDATE_ACCOUNT_BALANCE ="UPDATE accounts SET cash = ? WHERE account_id = ?";

  public static final String SELECT_PORTFOLIO_ID ="SELECT id FROM portfolio WHERE account_id = ? AND symbol = ?";

  public static final String INSERT_TRADE = """
    
  insert into trades(portfolio_id,account_id, username, exchange, symbol,  
                    stock_name, units, action, trade_date, price, currency, total)
          values (?,?, ?, ?, ? ,?, ?,?,?,?,?,?);

""";

public static final String FIND_TOTAL_TRADES_BY_ACCOUNTID_AND_SYMBOL ="SELECT COUNT(*) FROM trades WHERE symbol = ? AND account_id = ?";


public static final String ADD_CASH_TO_ACCOUNT_BY_ACCOUNTID ="UPDATE accounts SET cash = cash + ? WHERE account_id = ?";


public static final String DELETE_UNITS_FROM_PORTFOLIO_BY_ACCOUNTID_AND_SYMBOL ="UPDATE portfolio SET units = units - ? WHERE symbol = ? AND account_id = ?";



}

