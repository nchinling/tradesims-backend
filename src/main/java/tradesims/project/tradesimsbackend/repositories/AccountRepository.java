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
        new TradeRowMapper() , new Object[]{accountId, symbol});
        
        if (!trades.isEmpty()) {
            return Optional.of(trades.get(0));
        } else {
            return Optional.empty();
        }
    
    }

}
