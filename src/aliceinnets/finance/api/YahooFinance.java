package aliceinnets.finance.api;

public class YahooFinance {
	
	public final static String BASE_URL = "http://finance.yahoo.com/d/quotes.csv";
	
	public static String generateUrl(String[] symbols, String[] dataSymbols) {
		String symbol = symbols[0];
		for(int i=1;i<symbols.length;++i) symbol += ","+symbols[i];
		String dataSymbol = dataSymbols[0];
		for(int i=1;i<dataSymbols.length;++i) dataSymbol += dataSymbols[i];
		
		return BASE_URL+"?s="+symbol+"&f="+dataSymbol;
	}

}
