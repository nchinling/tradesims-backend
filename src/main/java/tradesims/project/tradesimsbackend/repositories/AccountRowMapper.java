package tradesims.project.tradesimsbackend.repositories;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import tradesims.project.tradesimsbackend.models.Account;

public class AccountRowMapper implements RowMapper<Account> {
    
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {

        Account account = new Account();

        account.setAccountId(rs.getString("account_id"));
        account.setName(rs.getString("name"));
        account.setUsername(rs.getString("username"));
        account.setPassword(rs.getString("password"));
        account.setEmail(rs.getString("email"));
        BigDecimal cash = rs.getBigDecimal("cash");
        if (cash != null) {
            account.setCash(cash.doubleValue()); 
        }
        return account;

    }
}

