package tradesims.project.tradesimsbackend.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import tradesims.project.tradesimsbackend.models.Account;
import tradesims.project.tradesimsbackend.models.Trade;
import tradesims.project.tradesimsbackend.services.AccountException;
import tradesims.project.tradesimsbackend.services.AccountService;


@Controller
@RequestMapping(path="/api")
@CrossOrigin(origins="*")
public class AccountController {
    
    @Autowired
    private AccountService accSvc;


	@PostMapping(path="/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public ResponseEntity<String> login(@RequestBody MultiValueMap<String, String> form) throws Exception {

        String email = form.getFirst("email");
        String password = form.getFirst("password");

        System.out.printf(">>> I am inside Controller Login >>>>>\n");
        JsonObject resp = null;

            Account loggedInAccount;
            try {
                loggedInAccount = accSvc.loginAccount(email, password);
                resp = Json.createObjectBuilder()
                .add("account_id", loggedInAccount.getAccountId())
                .add("username", loggedInAccount.getUsername())
                .add("cash", loggedInAccount.getCash())
                .add("timestamp", (new Date()).toString())
                .build();
            } catch (AccountNotFoundException | IOException e) {
                String errorMessage = e.getMessage();
                System.out.printf(">>>Account Exception occured>>>>>\n");   
                resp = Json.createObjectBuilder()
                .add("error", errorMessage)
                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resp.toString());
            }
         
        System.out.printf(">>>Successfully logged in>>>>>\n");   

        return ResponseEntity.ok(resp.toString());

    }


	@PostMapping(path="/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
	public ResponseEntity<String> register(@RequestBody MultiValueMap<String, String> form) {

        System.out.printf(">>> I am inside Controller Register>>>>>\n");

        String accountId = form.getFirst("account_id");
        String name = form.getFirst("name");
        String username = form.getFirst("username");
        String password = form.getFirst("password");
        String email = form.getFirst("email");

        System.out.println(">>> The accountId for update is >>>>>" + accountId);
        System.out.println(">>> The username for update is >>>>>" + username);
        System.out.println(">>> The password for update is >>>>>" + password);


        // For creation of new account
        Account account = new Account(accountId,name, username, email, password);
 
        JsonObject resp = null;

        try {
            accSvc.createAccount(account);
            Account loggedInAccount;
            try {
                loggedInAccount = accSvc.loginAccount(email, password);
                 System.out.println(">>> The starting cash is >>>>>" + loggedInAccount.getCash());
                resp = Json.createObjectBuilder()
                .add("account_id", loggedInAccount.getAccountId())
                .add("username", loggedInAccount.getUsername())
                .add("cash", loggedInAccount.getCash())
                .add("timestamp", (new Date()).toString())
                .build();
            } catch (AccountNotFoundException e) {
                String errorMessage = e.getMessage();
                System.out.printf(">>>Account Exception occured>>>>>\n");   
                resp = Json.createObjectBuilder()
                .add("error", errorMessage)
                .build();
                
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(resp.toString());
            } catch (IOException e) {
            
                e.printStackTrace();
            }

         System.out.printf(">>>Successfully registered>>>>>\n");   

        } catch (AccountException e) {
            String errorMessage = e.getMessage();
             System.out.printf(">>>Account Exception occured>>>>>\n");   
            resp = Json.createObjectBuilder()
            .add("error", errorMessage)
            .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(resp.toString());
        }

  
        return ResponseEntity.ok(resp.toString());

    }


	@PostMapping(path="/savetoportfolio", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
	public ResponseEntity<String> saveToPortfolio(@RequestBody MultiValueMap<String, String> form) {

        System.out.printf(">>> I am inside saveToPortfolio>>>>>\n");

        String accountId = form.getFirst("account_id");
        String username = form.getFirst("username");
        String exchange = form.getFirst("exchange");
        String stockName = form.getFirst("stockName");
        String symbol = form.getFirst("symbol");
        String currency = form.getFirst("currency");
        String action = form.getFirst("action");
        Double units = Double.parseDouble(form.getFirst("units"));
        Double price = Double.parseDouble(form.getFirst("price"));
       
        String date = form.getFirst("date");

        System.out.println(">>> The accountId for update is >>>>>" + accountId);
        System.out.println(">>> The username for update is >>>>>" + username);
        System.out.println(">>> The price for update is >>>>>" + price);
        System.out.println(">>> The date for update is >>>>>" + date);

  // Extract the relevant parts of the date string
    String formattedDateString = date.substring(0, 33);

    // Parse the extracted date string
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z", Locale.US);
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(formattedDateString, inputFormatter);

    // Convert to LocalDate (ignoring time and timezone)
    LocalDate loggedDate = zonedDateTime.toLocalDate();
    
        // For creation of new trade
        Trade trade = new Trade(accountId, username, exchange, stockName, symbol, units, price, currency, action, loggedDate);
 
        JsonObject resp = null;

        try {
            Trade savedTrade = accSvc.saveToPortfolio(trade);
                resp = Json.createObjectBuilder()
                .add("account_id", savedTrade.getAccountId())
                .add("username", savedTrade.getUsername())
                .add("exchange", savedTrade.getExchange())
                .add("stockName", savedTrade.getStockName())
                .add("symbol", savedTrade.getSymbol())
                .add("units", savedTrade.getUnits())
                .add("price", savedTrade.getPrice())
                .add("date", savedTrade.getDate().toString())
                .build();

         System.out.printf(">>>Successfully saved to portfolio>>>>>\n");   

        } catch (AccountException e) {
            String errorMessage = e.getMessage();
             System.out.printf(">>>Portfolio Exception occured>>>>>\n");   
            resp = Json.createObjectBuilder()
            .add("error", errorMessage)
            .build();
            
     
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(resp.toString());
        }

        System.out.printf(">>>Sending back to portfolio client>>>>>\n");   
        return ResponseEntity.ok(resp.toString());

    }


    @GetMapping(path="/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getPortfolioList(@RequestParam(required=true) String accountId) throws IOException{
     
        System.out.println("I am in getPortfolio server");
        System.out.println(">>>>>>>>accountId in controller>>>>>" + accountId);
       
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		List<String> userPortfolioList = accSvc.getPortfolioList(accountId);
		userPortfolioList.stream()
			.map(each -> Json.createObjectBuilder()
						.add("symbol", each)

						.build()
			)
			.forEach(json -> arrBuilder.add(json));

		return ResponseEntity.ok(arrBuilder.build().toString());
       
    }


    @DeleteMapping(path="/tradesList", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteFromPortfolio(@RequestBody Trade deleteTradeJson) {


    System.out.println(">>>>The symbol received is>>>" + deleteTradeJson.getSymbol() );
    System.out.println(">>>>The accountId received is>>>" + deleteTradeJson.getAccountId());
    System.out.println(">>>>The date received for delete is>>>" + deleteTradeJson.getDate());
   

    // Date date;
    JsonObject resp= null;
        // date = dateFormat.parse(sellDataJson.getDate());
        Trade deletedTrade = accSvc.deleteTrade(deleteTradeJson);
        resp = Json.createObjectBuilder()
            // .add("symbol", deletedSymbol)
            .add("symbol", deletedTrade.getSymbol())
            .add("account_id", deletedTrade.getAccountId())
            .add("username", deletedTrade.getUsername())
            .add("currency", deletedTrade.getCurrency())
            .add("exchange", deletedTrade.getExchange())
            .add("stockName", deletedTrade.getStockName())
            .add("units", deletedTrade.getUnits())
            .add("price", deleteTradeJson.getPrice())
            .add("date", deletedTrade.getDate().toString())
            .build();

        return ResponseEntity.ok(resp.toString());

    }

}
    
