package tradesims.project.tradesimsbackend.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Chart {
     private String symbol;
    private String interval;
    private String[] dateTime;
    private Double[] open;
    private Double[] high;
    private Double[] low;
    private Double[] close;
    private Double[] indicatorData;

    public Chart() {
    }
    public Chart(String symbol, String interval, String[] dateTime, Double[] open, Double[] high, Double[] low,
            Double[] close) {
        this.symbol = symbol;
        this.interval = interval;
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public Chart(String symbol, String interval, String[] dateTime, Double[] indicatorData) {
        this.symbol = symbol;
        this.interval = interval;
        this.dateTime = dateTime;
        this.indicatorData = indicatorData;
    }
    public Chart(String symbol, String interval, String[] dateTime, Double[] open, Double[] high, Double[] low,
            Double[] close, Double[] indicatorData) {
        this.symbol = symbol;
        this.interval = interval;
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.indicatorData = indicatorData;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getInterval() {
        return interval;
    }
    public void setInterval(String interval) {
        this.interval = interval;
    }
    public String[] getDateTime() {
        return dateTime;
    }
    public void setDateTime(String[] dateTime) {
        this.dateTime = dateTime;
    }
    public Double[] getOpen() {
        return open;
    }
    public void setOpen(Double[] open) {
        this.open = open;
    }
    public Double[] getHigh() {
        return high;
    }
    public void setHigh(Double[] high) {
        this.high = high;
    }
    public Double[] getLow() {
        return low;
    }
    public void setLow(Double[] low) {
        this.low = low;
    }
    public Double[] getClose() {
        return close;
    }
    public void setClose(Double[] close) {
        this.close = close;
    }
    public Double[] getIndicatorData() {
        return indicatorData;
    }
    public void setIndicatorData(Double[] indicatorData) {
        this.indicatorData = indicatorData;
    }

    @Override
    public String toString() {
        return "Chart [symbol=" + symbol + ", interval=" + interval + ", dateTime=" + Arrays.toString(dateTime)
                + ", open=" + Arrays.toString(open) + ", high=" + Arrays.toString(high) + ", low="
                + Arrays.toString(low) + ", close=" + Arrays.toString(close) + ", indicatorData="
                + Arrays.toString(indicatorData) + "]";
    }
    public static Chart createUserObject(String json) throws IOException {
        Chart c = new Chart();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            JsonObject meta = o.getJsonObject("meta");
            // c.setSymbol(meta.getJsonObject("symbol").toString());
            // c.setInterval(meta.getJsonObject("interval").toString());
            c.setSymbol(meta.getString("symbol"));
            c.setInterval(meta.getString("interval"));
            int valuesSize = o.getJsonArray("values").size();

        
        c.setDateTime(new String[valuesSize]);
        c.setOpen(new Double[valuesSize]);
        c.setClose(new Double[valuesSize]);
        c.setHigh(new Double[valuesSize]);
        c.setLow(new Double[valuesSize]);

        JsonArray values = o.getJsonArray("values");
        for (int i = 0; i < valuesSize; i++) {
            JsonObject dataObject = values.getJsonObject(i);
            c.getDateTime()[i] = dataObject.getString("datetime");
            c.getOpen()[i] = Double.parseDouble(dataObject.getString("open"));
            c.getClose()[i] = Double.parseDouble(dataObject.getString("close"));
            c.getHigh()[i] = Double.parseDouble(dataObject.getString("high"));
            c.getLow()[i] = Double.parseDouble(dataObject.getString("low"));
        }
    }
    return c;

    }


    public static Chart createUserTechnicalObject(String json, String indicator) throws IOException {
    Chart c = new Chart();
    try(InputStream is = new ByteArrayInputStream(json.getBytes())){
        JsonReader r = Json.createReader(is);
        JsonObject o = r.readObject();
        JsonObject meta = o.getJsonObject("meta");

        c.setSymbol(meta.getString("symbol"));
        c.setInterval(meta.getString("interval"));
        int valuesSize = o.getJsonArray("values").size();

        c.setDateTime(new String[valuesSize]);
        c.setIndicatorData(new Double[valuesSize]);


        JsonArray values = o.getJsonArray("values");
        for (int i = 0; i < valuesSize; i++) {
            JsonObject dataObject = values.getJsonObject(i);
            c.getDateTime()[i] = dataObject.getString("datetime");
            c.getIndicatorData()[i] = Double.parseDouble(dataObject.getString(indicator));
        }
    }

    System.out.println("Successfully created ChartTechnicalData");
    return c;

    }


}
