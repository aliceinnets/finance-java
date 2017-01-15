package aliceinnets.finance.api;

import java.io.IOException;
import org.jsoup.Jsoup;

/**
 * This class provides historical data (close, high, low, open and volume) on the requested stocks 
 * by retrieving quotes from Google Finance API, {@linkplain http://www.google.com/finanace/getprices}.
 * 
 * @author alice<aliceinnets@gmail.com>
 *
 */
public class GoogleFinanceGetprices {
	
	public final static String BASE_URL = "http://www.google.com/finance/getprices"; 
	
	String symbol;
	double intervalInput;
	String period;
	String exchangeInput;
	String columns;
	
	String url;
	String bodyText;
	String exchange;
	double marketOpenMinute;
	double marketCloseMinute;
	double interval;
	String[] dataColumns;
	String dataLog;
	double timezoneOffset;
	double[][] data;
	
	
	
	
	public GoogleFinanceGetprices(String symbol) {
		this(symbol, 86400.0, null, null);
	}
	
	public GoogleFinanceGetprices(String symbol, double interval) {
		this(symbol, interval, null, null);
	}
	
	public GoogleFinanceGetprices(String symbol, String period) {
		this(symbol, 86400.0, period, null);
	}
	
	public GoogleFinanceGetprices(String symbol, double interval, String period) {
		this(symbol, interval, period, null);
	}
	
	/**
	 * Getting historical data on the requested stocks by retrieving quotes from Google Finance API.  
	 * 
	 * @param symbol stock symbol
	 * @param interval time interval in seconds, default value is 86400 seconds (1 day)
	 * @param period period which data covers up to now, e.g. "1y" (1 year), "15d" (15 days), default value is "30d" (30 days) 
	 * @param exchange stock exchange symbol, e.g. NASD
	 */
	public GoogleFinanceGetprices(String symbol, double interval, String period, String exchange) {
		this.symbol = symbol;
		this.intervalInput = interval;
		this.period = period;
		this.exchangeInput = exchange;
		
		update();
	}
	
	
	public static String generateUrl(String symbol, double interval, String period, String exchange, String columns) {
		String url = BASE_URL+"?q="+symbol;
		if(interval > 0.0) url += "&i="+interval;
		if(period != null) url += "&p="+period;
		if(exchange != null) url += "&x="+exchange;
		if(columns != null) url += "&f="+columns;		
				
		return url;
	}
	
	
	public void update() {
		try {
			url = generateUrl(symbol, intervalInput, period, exchangeInput, columns);
			
			bodyText = Jsoup.connect(url).get().text();
			String[] bodyTextLines = bodyText.split(" ");
			
			exchange = bodyTextLines[0].replace("EXCHANGE", "");
			marketOpenMinute = Double.parseDouble(bodyTextLines[1].replace("MARKET_OPEN_MINUTE=", ""));
			marketCloseMinute = Double.parseDouble(bodyTextLines[2].replace("MARKET_CLOSE_MINUTE=", ""));
			interval = Double.parseDouble(bodyTextLines[3].replace("INTERVAL=", ""));
			dataColumns = bodyTextLines[4].replace("COLUMNS=", "").split(",");
			dataLog = bodyTextLines[5].replace("DATA", "");
			timezoneOffset = Double.parseDouble(bodyTextLines[6].replace("TIMEZONE_OFFSET=", ""));
			
			data = new double[dataColumns.length][bodyTextLines.length-7];
			
			String[] dataStringHeader = bodyTextLines[7].split(",");
			double firstTimeStamp = Double.parseDouble(dataStringHeader[0].replace("a", ""));
			
			data[0][0] = firstTimeStamp;
			for(int i=1;i<dataStringHeader.length;++i) {
				data[i][0] = Double.parseDouble(dataStringHeader[i]);
			}
			
			for(int j=1;j<data[0].length;++j) {
				String[] dataString = bodyTextLines[j+7].split(",");
				if(dataString[0].contains("a")) {
					firstTimeStamp = Double.parseDouble(dataString[0].replace("a", ""));
					data[0][j] = firstTimeStamp;
				} else {
					data[0][j] = firstTimeStamp + interval * Double.parseDouble(dataString[0]);
				}
				
				for(int i=1;i<data.length;++i) {
					data[i][j] = Double.parseDouble(dataString[i]);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	
	public String getUrl() {
		return url;
	}

	public String getBodyText() {
		return bodyText;
	}

	public String getExchange() {
		return exchange;
	}

	public double getMarketOpenMinute() {
		return marketOpenMinute;
	}

	public double getMarketCloseMinute() {
		return marketCloseMinute;
	}

	public double getTimeInterval() {
		return interval;
	}

	public String[] getDataColumns() {
		return dataColumns;
	}

	public String getDataLog() {
		return dataLog;
	}

	public double getTimezoneOffset() {
		return timezoneOffset;
	}

	public double[][] getData() {
		return data;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
		update();
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
		update();
	}

	public void setInterval(double intervalInput) {
		this.intervalInput = intervalInput;
		update();
	}

	public void setExchange(String exchangeInput) {
		this.exchangeInput = exchangeInput;
		update();
	}
	
}

