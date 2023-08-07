package tradesims.project.tradesimsbackend.repositories;

public class DBQueries {
    public static final String INSERT_REGISTRATION = """
        
    insert into accounts(account_id, name, username, email, password)
                      values (?, ?, ?, ? ,?);

  """;

public static final String SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL = """
    SELECT t.account_id, t.symbol, t.username, t.exchange, t.stock_name,
    t.currency, SUM(t.units*t.buy_price)/SUM(t.units) as buy_price, 
    SUM(t.total) AS total_sum, SUM(t.units) AS total_units, 
    SUM(t.fee) AS total_fee
    FROM trades AS t
    RIGHT JOIN portfolio AS p ON t.portfolio_id = p.id
    WHERE t.account_id = ? AND t.symbol = ?
    GROUP BY t.account_id, t.symbol, t.username, t.exchange, t.stock_name, t.currency
    ORDER BY t.symbol
""";

  public static final String SELECT_ACCOUNT_BY_EMAIL ="select * from accounts where email = ?";
    
  public static final String CHECK_ACCOUNTID_EXISTS = "SELECT COUNT(*) FROM accounts WHERE account_id = ?";


}
