package tradesims.project.tradesimsbackend.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import tradesims.project.tradesimsbackend.models.Account;
import tradesims.project.tradesimsbackend.models.Trade;

import static tradesims.project.tradesimsbackend.repositories.DBQueries.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.AccountException;

@Repository
public class AccountRepository {
    
    @Autowired 
    JdbcTemplate jdbcTemplate;

    //insert into accounts table
    public boolean createAccount(Account account){

        // Create new account
        return jdbcTemplate.update(INSERT_REGISTRATION, account.getAccountId(), account.getName(), 
                                account.getUsername(), account.getEmail(), account.getPassword()) > 0;

    }


    public Optional<Account> getAccountByUsername(String email){
        List<Account> accounts = jdbcTemplate.query(SELECT_ACCOUNT_BY_EMAIL, 
        new AccountRowMapper() , new Object[]{email});
        
        if (!accounts.isEmpty()) {
            return Optional.of(accounts.get(0));
        } else {
            return Optional.empty();
        }
        
    }

    public Optional<Trade> getTradeData(String accountId, String symbol){
        List<Trade> trades = jdbcTemplate.query(SELECT_TRADE_BY_ACCOUNTID_AND_SYMBOL, 
        new TotalTradeRowMapper() , new Object[]{accountId, symbol});
        
        if (!trades.isEmpty()) {
            return Optional.of(trades.get(0));
        } else {
            return Optional.empty();
        }
    
    }


    public Double getTotalUnits(String accountId, String symbol) {
        Double totalUnits=jdbcTemplate.queryForObject(SELECT_TOTAL_UNITS_BY_ACCOUNTID_AND_SYMBOL,Double.class, accountId, symbol);
        
        return totalUnits;
    }


    public List<String> getPortfolioList(String accountId) {
        List<String> portfolioSymbols=jdbcTemplate.queryForList(SELECT_SYMBOLS_BY_ACCOUNTID,String.class, accountId);
        
        return portfolioSymbols;
    }

    
    //insert into portfolio and trades table
    @Transactional(rollbackFor = AccountException.class)
    public Trade saveToPortfolio(Trade trade){

        // No need to insert new symbol into portfolio if symbol already exist
        System.out.println(">>>>>>>>trade.getDatenew is>>>>>>" + trade.getDate());
        
        Double cashTotal = jdbcTemplate.queryForObject(SELECT_ACCOUNT_BALANCE, Double.class, trade.getAccountId());
        Double newCashTotal = cashTotal - trade.getTotal();
        jdbcTemplate.update(UPDATE_ACCOUNT_BALANCE, newCashTotal, trade.getAccountId(

        
        ));
    
        jdbcTemplate.update(INSERT_UPDATE_PORTFOLIO, trade.getAccountId(), trade.getSymbol(), trade.getUnits(), trade.getUnits());

        // String articleId = UUID.randomUUID().toString().substring(0, 8);
        String portfolioId = jdbcTemplate.queryForObject(SELECT_PORTFOLIO_ID, String.class, trade.getAccountId(), trade.getSymbol());
        //Insert into trades
        jdbcTemplate.update(INSERT_TRADE, portfolioId, trade.getAccountId(), trade.getUsername(), 
                                trade.getExchange(), trade.getSymbol(), trade.getStockName(),
                                trade.getUnits(), trade.getAction(), trade.getDate(), trade.getPrice(), trade.getCurrency(), trade.getTotal()); 

        return trade;
    }


    public Trade deleteTrade(Trade tradeToSell) {

        Double totalUnits=jdbcTemplate.queryForObject(SELECT_TOTAL_UNITS_BY_ACCOUNTID_AND_SYMBOL,Double.class, tradeToSell.getAccountId(), tradeToSell.getSymbol());
       
        if (totalUnits < tradeToSell.getUnits()){
            throw new IllegalStateException("Not enough units to sell.");
        }
        else{

            jdbcTemplate.update(DELETE_UNITS_FROM_PORTFOLIO_BY_ACCOUNTID_AND_SYMBOL, tradeToSell.getUnits(), tradeToSell.getSymbol(), tradeToSell.getAccountId());
            jdbcTemplate.update(ADD_CASH_TO_ACCOUNT_BY_ACCOUNTID, tradeToSell.getUnits()*tradeToSell.getPrice(), tradeToSell.getAccountId());
            Double unitsLeft=jdbcTemplate.queryForObject(SELECT_TOTAL_UNITS_BY_ACCOUNTID_AND_SYMBOL,Double.class, tradeToSell.getAccountId(), tradeToSell.getSymbol());
            System.out.println(">>>unitsLeft is " + unitsLeft);
       
            String portfolioId = jdbcTemplate.queryForObject(SELECT_PORTFOLIO_ID, String.class, tradeToSell.getAccountId(), tradeToSell.getSymbol());
            jdbcTemplate.update(INSERT_TRADE, portfolioId, tradeToSell.getAccountId(), tradeToSell.getUsername(), 
                                tradeToSell.getExchange(), tradeToSell.getSymbol(), tradeToSell.getStockName(),
                                tradeToSell.getUnits(), tradeToSell.getAction(), tradeToSell.getDate(), tradeToSell.getPrice(),tradeToSell.getCurrency(),tradeToSell.getUnits()*tradeToSell.getPrice());
            
            // if (unitsLeft == 0){
            //     jdbcTemplate.update(DELETE_ROW_FROM_PORTFOLIO_BY_ACCOUNTID, tradeToSell.getAccountId(), tradeToSell.getSymbol());
            // }
            
        }

        return tradeToSell;
    }

  

}
