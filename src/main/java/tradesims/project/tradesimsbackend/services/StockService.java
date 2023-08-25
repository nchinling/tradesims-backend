package tradesims.project.tradesimsbackend.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;

import tradesims.project.tradesimsbackend.models.Portfolio;
import tradesims.project.tradesimsbackend.models.Stock;
import tradesims.project.tradesimsbackend.models.StockInfo;
import tradesims.project.tradesimsbackend.models.StockProfile;
import tradesims.project.tradesimsbackend.models.Trade;
import tradesims.project.tradesimsbackend.repositories.AccountRepository;
import tradesims.project.tradesimsbackend.repositories.StockRepository;

@Service
public class StockService {

    @Value("${twelve.data.url}")
    private String twelveDataUrl;

    @Value("${twelve.data.key}")
    private String twelveDataApiKey;

    @Value("${twelve.data.host}")
    private String twelveDataApiHost;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private AccountRepository accountRepo;
    
    //function to get info from an external server using API.
   public Optional<Stock> getStockData(String symbol, String interval)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/quote")
                           .queryParam("symbol", symbol)
                           .queryParam("interval", interval)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange: >>>>" + r.getBody());
       Stock s = Stock.createUserObject(r.getBody());

       //for debugging
       String apiSymbol = s.getSymbol();
       String name = s.getName();
       System.out.println(">>>apiSymbol: " + apiSymbol);
       System.out.println(">>>name: " + name);

       return Optional.of(s);

   }

   public Optional<Stock> getLivePrice(String symbol, String interval)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/price")
                           .queryParam("symbol", symbol)
                           .queryParam("interval", interval)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange: >>>>" + r.getBody());
       Stock s = Stock.createPriceObject(r.getBody());

       //for debugging
       Double livePrice = s.getLivePrice();
    
       System.out.println(">>>LivePrice: " + livePrice);

       return Optional.of(s);

   }


    //function to get info from an external server using API.
   public Optional<StockProfile> getStockProfile(String symbol)
   throws IOException{
       System.out.println("twelveDataUrl: " + twelveDataUrl);
       System.out.println("twelveDataApiKey: " + twelveDataApiKey);
       System.out.println("twelveDataApiHost: " + twelveDataApiHost);
   
       String stockUrl = UriComponentsBuilder
                           .fromUriString(twelveDataUrl+"/profile")
                           .queryParam("symbol", symbol)
                           .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Key", twelveDataApiKey);
        headers.set("X-RapidAPI-Host", twelveDataApiHost);


        RequestEntity req = RequestEntity.get(stockUrl)
                    .headers(headers)
                    .build();

       RestTemplate template= new RestTemplate();
        ResponseEntity<String> r  = template.exchange(req, 
        String.class);


       //r.getBody is a string response from api.
        System.out.println(">>>managed to exchange: >>>>" + r.getBody());
       StockProfile sp = StockProfile.createUserObject(r.getBody());

       //for debugging
       String apiSymbol = sp.getSymbol();
       String name = sp.getName();
       System.out.println(">>>apiSymbol: " + apiSymbol);
       System.out.println(">>>name: " + name);

       return Optional.of(sp);

   }


    public Optional<StockProfile> getStockLogo(String symbol) throws IOException{
    System.out.println("twelveDataUrl: " + twelveDataUrl);
    System.out.println("twelveDataApiKey: " + twelveDataApiKey);
    System.out.println("twelveDataApiHost: " + twelveDataApiHost);

    String stockUrl = UriComponentsBuilder
                        .fromUriString(twelveDataUrl+"/logo")
                        .queryParam("symbol", symbol)
                        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("X-RapidAPI-Key", twelveDataApiKey);
    headers.set("X-RapidAPI-Host", twelveDataApiHost);


    RequestEntity req = RequestEntity.get(stockUrl)
                .headers(headers)
                .build();

    RestTemplate template= new RestTemplate();
    ResponseEntity<String> r  = template.exchange(req, 
    String.class);


    //r.getBody is a string response from api.
    System.out.println(">>>managed to exchange logo: >>>>" + r.getBody());
    StockProfile spl = StockProfile.createLogo(r.getBody());

    //for debugging
    String stockLogo = spl.getLogoUrl();
    System.out.println(">>>stockLogo: " + stockLogo);

    return Optional.of(spl);

}


public List<StockInfo> getStocksList(String exchange, String filter, int limit, int skip) {
System.out.println(">>>>>>>> I am in Service >>> getStocksList");
return stockRepo.getStocksList(exchange, filter, limit, skip);
}


public Portfolio getPortfolioData(String accountId, String symbol, String interval) throws IOException {
    System.out.println(">>>>>>>> I am in getPortfolioDataService>>>>>>");
    Optional<Stock> s;
    Optional<Trade> t = accountRepo.getTradeData(accountId, symbol);
    Trade trade = t.get();
    Double totalUnits = accountRepo.getTotalUnits(accountId, symbol);

    if(totalUnits == 0){
        Portfolio p = new Portfolio(trade.getAccountId(), trade.getSymbol(), trade.getStockName(),
        trade.getExchange(), trade.getCurrency(), totalUnits, trade.getPrice(), totalUnits*trade.getPrice(),
        0.0, 0.0, 
        0.0, 0.0, LocalDate.now());
        return p;
   
    }
    else{
   Stock stock;
    s = getLivePrice(symbol, interval);
    stock = s.get();
    System.out.println("The live price is " + stock.getLivePrice());

    Portfolio calculatedP = getCalculations(totalUnits, stock.getLivePrice(),totalUnits*trade.getPrice());
    System.out.println(">>>>The total percentage change is >>>>>" + calculatedP.getTotalPercentageChange() );
    Portfolio p = new Portfolio(trade.getAccountId(), trade.getSymbol(), trade.getStockName(),
    trade.getExchange(), trade.getCurrency(), totalUnits, trade.getPrice(), totalUnits*trade.getPrice(),
    stock.getLivePrice(), calculatedP.getTotalCurrentPrice(), 
    calculatedP.getTotalReturn(), calculatedP.getTotalPercentageChange(), LocalDate.now()  );
    System.out.println(">>> The stock price is>>>" + stock.getLivePrice());

    return p;
 

    }
    
}

    private Portfolio getCalculations(Double units, Double currentUnitPrice, Double totalBuyPrice){
        System.out.println("The current unit price is " + currentUnitPrice);
        Double totalCurrentPrice = units*currentUnitPrice; 
        Double totalReturn = totalCurrentPrice - totalBuyPrice;
        Double totalPercentageChange = (totalReturn/totalBuyPrice)*100;
        System.out.println("The total current price is " + totalCurrentPrice);
        System.out.println("The total buy price is " + totalBuyPrice);
        
        Portfolio p = new Portfolio(totalCurrentPrice, totalReturn, totalPercentageChange);
        return p;
    }

    public Optional<Stock> getStockFromRedis(String symbol, String interval) throws IOException{
        System.out.println(">>>>>>>> I am in Redis service for stocks>>>>>>");
        return stockRepo.getStockFromRedis(symbol, interval);
    }

    public Optional<StockProfile> getStockProfileFromRedis(String symbol) throws IOException{
        System.out.println(">>>>>>>> I am in Redis service for stocksProfile>>>>>>");
        return stockRepo.getStockProfileFromRedis(symbol);
    }

    public void saveStockProfile(StockProfile stockProfile){
        stockRepo.saveStockProfile(stockProfile);
    }

    public void saveStockData(Stock stock, String interval){
        stockRepo.saveStockData(stock, interval);
    }


    public List<Trade> getAllTradesData(String accountId) throws IOException {
        System.out.println(">>>>>>>> I am in getPortfolioDataService>>>>>>");
        // Optional<Stock> s;
     
        List<Trade> allTrades = stockRepo.getAllTradesData(accountId);

        
        List<Trade> trades = new ArrayList<Trade>();
        if (!allTrades.isEmpty()) {
        for (Trade trade : allTrades) {

            Trade eachTrade = new Trade(trade.getAccountId(), trade.getUsername(), trade.getExchange(), trade.getStockName(),
            trade.getSymbol(),  trade.getUnits(),  trade.getPrice(), trade.getCurrency(),trade.getAction(), trade.getDate(),  trade.getTotal()
            );

            trades.add(eachTrade);
        }
        } else {
            System.out.println("No trades found.");
        }
    
        return trades;
        
    }
    


}
