package tradesims.project.tradesimsbackend.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import tradesims.project.tradesimsbackend.models.Portfolio;
import tradesims.project.tradesimsbackend.models.Stock;
import tradesims.project.tradesimsbackend.models.StockInfo;
import tradesims.project.tradesimsbackend.models.StockProfile;
import tradesims.project.tradesimsbackend.models.Trade;
import tradesims.project.tradesimsbackend.services.StockService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
// @CrossOrigin(origins = "http://localhost:4200")
public class StockController {
    
    @Autowired
    private StockService stockSvc;

    @GetMapping(path="/quote/stock")
    @ResponseBody
    public ResponseEntity<String> getStockData(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1min",required=false) String interval,
    @RequestParam(required=false) String username) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getStockData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
        
        //Obtain from redis cache
        Optional<Stock> stk = stockSvc.getStockFromRedis(symbol, interval);
        if (stk.isPresent()){
            Stock stock = stk.get();
          
            System.out.println("Obtained stock data from Redis");
     
            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stock.getSymbol())
                .add("name", stock.getName())
                .add("exchange",stock.getExchange() )
                .add("currency", stock.getCurrency())
                .add("open", stock.getOpen())
                .add("high", stock.getHigh())
                .add("low", stock.getLow())
                .add("close", stock.getClose())
                .add("real_time_price", stock.getLivePrice())
                .add("previous_close", stock.getPreviousClose())
                .add("volume", stock.getVolume())
                .add("change", stock.getChange())
                .add("percent_change", stock.getPercentChange())
                .add("datetime", stock.getDatetime())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        }
        
        //Obtain from api
        Optional<Stock> s = stockSvc.getStockData(symbol, interval);
        Optional<Stock> s2 = stockSvc.getLivePrice(symbol, interval);

        if (s.isPresent() && s2.isPresent()) {
            Stock stock = s.get();
            Stock stock2 = s2.get();
            stock.setLivePrice(stock2.getLivePrice());

            System.out.println("The live price saved from API is " + stock.getLivePrice());

            //save stock data in redis/mongo for quick retrieval
            stockSvc.saveStockData(stock, interval);

            System.out.println("Obtained stock data from API");

            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stock.getSymbol())
                .add("name", stock.getName())
                .add("exchange",stock.getExchange() )
                .add("currency", stock.getCurrency())
                .add("open", stock.getOpen())
                .add("high", stock.getHigh())
                .add("low", stock.getLow())
                .add("close", stock.getClose())
                .add("real_time_price", stock.getLivePrice())
                .add("previous_close", stock.getPreviousClose())
                .add("volume", stock.getVolume())
                .add("change", stock.getChange())
                .add("percent_change", stock.getPercentChange())
                .add("datetime", stock.getDatetime())
                .build();
                System.out.println(">>>resp: " + resp);
            
            return ResponseEntity.ok(resp.toString());
        } 

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Stock information not available for the provided symbol.");
        
        
    }

    
    @GetMapping(path="/stock/profile/{symbol}")
    @ResponseBody
    public ResponseEntity<String> getStockProfile(@PathVariable String symbol) throws IOException{

        System.out.println(">>>>>>>>Symbol in StockProfile>>>>>" + symbol);
    
        //Obtain from redis cache
        Optional<StockProfile> osp = stockSvc.getStockProfileFromRedis(symbol);
        if (osp.isPresent()){
            System.out.println(">>>>>>>>Retrieved StockProfile from redi>>>>>" + symbol);
            StockProfile stockProfile = osp.get();
          
            System.out.println("Obtained stock data from Redis");
     
            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stockProfile.getSymbol())
                .add("name", stockProfile.getName())
                .add("sector", stockProfile.getSector())
                .add("industry", stockProfile.getIndustry())
                .add("ceo", stockProfile.getCeo())
                .add("employees", stockProfile.getEmployees())
                .add("website", stockProfile.getWebsite())
                .add("description", stockProfile.getDescription())
                .add("logoUrl", stockProfile.getLogoUrl())
                .build();
                System.out.println(">>>built the stock profile>>>> " + resp);
            
            return ResponseEntity.ok(resp.toString());
        }
        
        //Obtain from api
        Optional<StockProfile> sp = stockSvc.getStockProfile(symbol);
        Optional<StockProfile> spl = stockSvc.getStockLogo(symbol);
        if (sp.isPresent()) {
            StockProfile stockProfile = sp.get();
        
            StockProfile stockLogo = spl.get();
            stockProfile.setLogoUrl(stockLogo.getLogoUrl());

            //save stock data in redis/mongo for quick retrieval
            stockSvc.saveStockProfile(stockProfile);

            System.out.println("Obtained stock data from API");

            JsonObject resp = Json.createObjectBuilder()
                .add("symbol", stockProfile.getSymbol())
                .add("name", stockProfile.getName())
                .add("sector", stockProfile.getSector())
                .add("industry", stockProfile.getIndustry())
                .add("ceo", stockProfile.getCeo())
                .add("employees", stockProfile.getEmployees())
                .add("website", stockProfile.getWebsite())
                .add("description", stockProfile.getDescription())
                .add("logoUrl", stockProfile.getLogoUrl())
                .build();
                System.out.println(">>>built the stock profile>>>> " + resp);
            
            return ResponseEntity.ok(resp.toString());
        } 

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Stock Profile not available for the provided symbol.");
        
    }


    @GetMapping(path="/stocklist", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public ResponseEntity<String> getStocks(
            @RequestParam(defaultValue = "") String exchange,
			@RequestParam(defaultValue = "") String filter,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "0") int skip) {
    

		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        System.out.println(">>>The exchange in controller is >>> " + exchange);
		List<StockInfo> stockInfoList = stockSvc.getStocksList(exchange,filter, skip, limit);
		stockInfoList.stream()
			.map(stockinfo -> Json.createObjectBuilder()
						.add("name", stockinfo.name())
						.add("symbol", stockinfo.symbol())
                        .add("currency", stockinfo.currency())
						.add("exchange", stockinfo.exchange())
                        .add("country", stockinfo.country())
						.add("type", stockinfo.type())
						.build()
			)
			.forEach(json -> arrBuilder.add(json));

		return ResponseEntity.ok(arrBuilder.build().toString());
	}


    @GetMapping(path="/quote/portfolio")
    @ResponseBody
    public ResponseEntity<String> getPortfolioData(@RequestParam(required=true) String symbol,
    @RequestParam(defaultValue = "1min",required=false) String interval,
    @RequestParam(required=false) String account_id) throws IOException{
        // Integer num = weatherSvc.getWeather(city);
        System.out.println("I am in getStockData server");
        System.out.println(">>>>>>>>Symbol in controller>>>>>" + symbol);
        System.out.println(">>>>>>>>Interval in controller>>>>>" + interval);
        System.out.println(">>>>>>>>accountId in controller>>>>>" + account_id);

        String accountId = account_id;
        
            Portfolio portfolio = stockSvc.getPortfolioData(accountId, symbol, interval);

            //save portfolio data in redis/mongo for quick retrieval
            // stockSvc.saveStockData(stock, interval);

            System.out.println("Obtained portfolio data from API");
            System.out.println(">>>>>Portfolio stockname is>>>>" + portfolio.getStockName());
            System.out.println(">>>>>Units after deletion is>>>>" + portfolio.getUnits());

            JsonObject resp = Json.createObjectBuilder()
                .add("account_id", portfolio.getAccountId())
                .add("symbol", portfolio.getSymbol())
                .add("stock_name", portfolio.getStockName())
                .add("exchange", portfolio.getExchange())
                .add("currency", portfolio.getCurrency())
                .add("units", portfolio.getUnits())
                .add("buy_unit_price", portfolio.getBuyUnitPrice())
                .add("buy_total_price", portfolio.getBuyTotalPrice())
                .add("unit_current_price", portfolio.getUnitCurrentPrice())
                .add("total_current_price", portfolio.getTotalCurrentPrice())
                .add("total_return", portfolio.getTotalReturn())
                .add("total_percentage_change", portfolio.getTotalPercentageChange())
                .add("datetime", portfolio.getDateTime().toString())
                .build();
                System.out.println(">>>resp: " + resp);
                System.out.println(">>>sending back portfolio data.>>>>>Hooray: ");

            System.out.println(">>>>>Portfolio" + portfolio.getStockName() +"total units is>>>>" + portfolio.getUnits());
            return ResponseEntity.ok(resp.toString());

    }


    @GetMapping(path="/allTrades")
    @ResponseBody
    public ResponseEntity<String> getAllTradesData(
    @RequestParam(required=true) String account_id) throws IOException{
        System.out.println("I am in allTrades server");
        System.out.println(">>>>>>>>accountId in controller>>>>>" + account_id);

        String accountId = account_id;
        
        List<Trade> trades = stockSvc.getAllTradesData(accountId);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (Trade trade : trades) {
            JsonObjectBuilder tradesBuilder = Json.createObjectBuilder()
            .add("account_id", trade.getAccountId())
            .add("username", trade.getUsername())
            .add("exchange", trade.getExchange())
            .add("currency", trade.getCurrency())
            .add("stockName", trade.getStockName())
            .add("symbol", trade.getSymbol())
            .add("units", trade.getUnits())
            .add("price", trade.getPrice())
            .add("total_price", trade.getTotal())
            .add("action", trade.getAction())
            .add("date", trade.getDate().toString());
            
            arrayBuilder.add(tradesBuilder);
        }

        JsonArray respArray = arrayBuilder.build();
        System.out.println(">>>sending back jsonarray tradesResponse data.>>>>>Hooray: " + respArray);
        return ResponseEntity.ok(respArray.toString());

}





}




