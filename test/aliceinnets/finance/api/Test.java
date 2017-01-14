package aliceinnets.finance.api;

import junit.framework.TestCase;

public class Test extends TestCase {
	
	public void testGoogleFinanceGetprices() {
		GoogleFinanceGetprices api = new GoogleFinanceGetprices("GOOG", 120.0, "1d");
		api.getData();
		
		api = new GoogleFinanceGetprices("GOOG", 120.0, "5d");
		double[][] data1 = api.getData();
		
		api.setPeriod("1d");
		double[][] data2 = api.getData();
		
	}

}
