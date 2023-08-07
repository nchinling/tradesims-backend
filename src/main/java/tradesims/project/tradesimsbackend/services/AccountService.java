package tradesims.project.tradesimsbackend.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.security.auth.login.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tradesims.project.tradesimsbackend.models.Account;
import tradesims.project.tradesimsbackend.repositories.AccountRepository;


@Service
public class AccountService {
    
    
    @Autowired
    private AccountRepository accountRepo;
    

        @Transactional(rollbackFor = AccountException.class)
        public Account createAccount(Account account) throws AccountException {
            try {
        
                //create Account id. 
                String accountId = UUID.randomUUID().toString().substring(0, 8);

                account.setAccountId(accountId);
                accountRepo.createAccount(account);

                return account;
            } catch (DataIntegrityViolationException ex) {
                String errorMessage = "Email has been taken.";
                throw new AccountException(errorMessage);
            }
        }


        @Transactional(rollbackFor = AccountException.class)
        public Account retrieveAccount(String username) throws AccountException {
            // try {
        
                Optional<Account> retrievedAccount = accountRepo.getAccountByUsername(username);

                return retrievedAccount.get();

            // } catch (DataIntegrityViolationException ex) {
            //     String errorMessage = "Error in retrieving account.";
            //     throw new AccountException(errorMessage);
            // }
        }


    public Account loginAccount(String email, String password) throws IOException, AccountNotFoundException {
        Optional<Account> userAccount = accountRepo.getAccountByUsername(email);

        if (userAccount.isPresent()) {
            Account loggedInAccount = userAccount.get();
            System.out.printf(">>>String Password is >>>" + password);
            System.out.printf(">>>loggedInAccountPassword is >>>" + loggedInAccount.getPassword());   
            if (loggedInAccount.getPassword().equals(password)){
                return loggedInAccount;
            }
            else{
                throw new AccountNotFoundException("Password is incorrect");
            }

        } else {
            throw new AccountNotFoundException("Account not found for email: " + email);
        }
    }

    
}









    

