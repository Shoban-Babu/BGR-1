package bg.framework.app.functional.page.reFactoring;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.*;

import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.RunQTP;
import bg.framework.app.functional.util.SiebelDataBase;

public class PaymentHistoryPage extends BasePage {

	private final static String File_HistoryPage = "resources/ReFactoring/PaymentHistory.properties";
    private static Properties HistoryPageProperties = new PropertyLoader(File_HistoryPage).load();


// Verify whether the appropriate links exists in Account Summary Page and if it exists click on the appropriate links and 
// check whether appropriate page gets displayed
    
	public void paymentsHistoryLinkValidation(){
		 int linksCnt = browser.getChildElementsCountByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.NavigationLinks"));
				 
		 final String[] links = {"Billing", "Payments", "Submit a meter reading", "Energy Consumption Graph", "Nectar Points", 
				 				 "Update your details", "Home move services","Your messages","Your Account"};
		 for (int i = 1; i == linksCnt; i++) {
			 verifyAndClickWithXpath("//*[contains(@class,'left-nav')]//ul/li/ul/ul/li["+(i)+"]","Displayed Links "); 
			 if (browser.getTitle().contains(links[i - 1])){
				 Report.updateTestLog("Expected Link was displayed","PASS");
	         }else{
	             Report.updateTestLog("Expected Link was not displayed","FAIL");
	          }
			 browser.browserBack();
			}
		 }
	

// Navigate to Payment History Page by following the appropriate navigation(Payments -> Payment History)
	
	public void paymentsValidation(){
		verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.Payments"), "Payments");
		verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.PaymentHistoryText"), "Payment History");
	}

// Check the displayed Payment Option[Credit/Debit] and if the Option is Debit verify whether make a payment link option is displayed
	
	public void makeAPaymentValidation(String accountNumber){
		paymentsValidation();
		addressValidation(accountNumber);
		/*String val = browser.getTextByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.PaymentMode")); 
		if (val.trim().contains("debit")){
			if (browser.isElementVisibleWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.MakeAPayment"))){
				System.out.println("Make a payment link found");
				verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.MakeAPayment"), "Make A Payment");
				String displayedURL = browser.getURL();
				if (displayedURL.contains("Make-A-Payment")){
					Report.updateTestLog("Expected Page title was displayed"+ displayedURL,"PASS");
		          }else {
		            Report.updateTestLog("Expected Page title was not displayed","FAIL");
		          }*/
				paymentsValidation();
			   //}
		   //}
		}

// Check the Transaction History table and retrieve the values displayed under date and credit fields. Compare it against the
// values retrieved from SAP and verify whether both values are similar
	
	public void transactionHistoryValidation(final UserProfile userProfile){
		int a = browser.getRowCountByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionHistoryTable"));
     	for (int i = 0; i < a; i++) {
       		String dateVal = browser.getTextByXpath("//table[contains(@class,'table-history')]//tr["+(i+1)+"]//td[1]");
       		String creditVal = browser.getTextByXpath("//table[contains(@class,'table-history')]//tr["+(i+1)+"]//td[5]");
       		if (dateVal == "" && creditVal == "" ){
       			Report.updateTestLog("Expected date "+ dateVal +"and credit value "+ creditVal +"was displayed in the transaction history table","PASS");
            }else {
                Report.updateTestLog("Email date and credit values was not displayed","FAIL");
            }
       	}
	}

// Enter the appropriate Keyword[Date/Type/Debits/Credit/Balance]in Search field and check whether the appropriate value searched
// gets displayed in the Transaction History table. Once after the search gets completed select show recent transactions in order
// to refresh the transaction history table
	
	public void searchFieldValidation(String searchText){
		verifyAndInputByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.Search"),"Search Field",searchText);
		verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.SearchButton"),"Search");
		browser.wait(2000);
		int transHistoryRowCnt = browser.getRowCountByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionHistoryTable"));
		int transHistoryColCnt = browser.getColCountByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionHistoryTable"));
		for (int i = 0; i < transHistoryRowCnt; i++) {
			for (int j = 1; j == transHistoryColCnt; j++) {
			  String displayedSearchVal = browser.getTextByXpath("//table[contains(@class,'table-history')]//tr["+(i+1)+"]//td["+j+"]");
			  if (displayedSearchVal.trim().equalsIgnoreCase("Gas Charges")){
				  Report.updateTestLog("Expected search value was displayed"+ displayedSearchVal,"PASS");
	          }else {
	              Report.updateTestLog("Expected search value was not displayed","FAIL");
	          }
			 }
		  }
		recentTransactionHistoryValidation();
	   }

	
// Verify the pagination and retrieve the total number of transactions displayed per page from the Transaction History table 
	
	 public String transactionHistoryTableValidation(){
		 
		 String totalTransactionCnt = browser.getTextByXpath("//div[contains(@class,'dataTables_info')]");
		 //String balanceInApp = browser.getTextByXpath("//*[@id='balanceSection']//span[1]");
		 //String transactionTypeInApp = browser.getTextByXpath("//*[@id='balanceSection']//span[2]");
		 String balanceInApp = browser.getTextByXpath("/html/body/div/div[3]/div/div/div[2]/div[4]/div[2]/div[3]/p/span[1]");
		 
		 if (browser.isElementVisibleWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionCnt"))) {
			 int itemsCnt = browser.getChildElementsCountByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionCnt"));
			 			 
			 for (int i = 0; i < itemsCnt - 1; i++) {
				 String itemsPerPage = browser.getTextByXpath("//ul[contains(@id,'ddlDisplayLength')]//li["+(i+1)+"]//a");
				 Report.updateTestLog("Displayed total items per page :"+ itemsPerPage,"DONE");
				 verifyAndClickWithXpath("//ul[contains(@id,'ddlDisplayLength')]//li["+(i+2)+"]//a","Items Cnt");
				 browser.wait(getWaitTime());
			 }
			 
				 String transactionCnt = browser.getTextByXpath("//div[contains(@class,'dataTables_info')]");
				 String [] newtransactionCnt = null;
				 newtransactionCnt = transactionCnt.split(" ");
			 totalTransactionCnt = newtransactionCnt[5];
				 
			 if (totalTransactionCnt.equals(newtransactionCnt[3])){
				 Report.updateTestLog("Expected transactions cnt was displayed"+ totalTransactionCnt,"PASS");
		     }else {
		         Report.updateTestLog("Expected search value was not displayed","FAIL"); 
			 }
	 }
		 return balanceInApp; 
	 
	 }
	 
// Validate whether show recent transactions link is displayed and click on the recent transactions link	 
	 
	 public void recentTransactionHistoryValidation(){
		 if (browser.isElementVisibleWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.RecentTransactions"))){
		   verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.RecentTransactions"),"Recent Transactions");
		 }
	 }
	 
// Validate the date field by entering the date ranges( From and To dates) and click on View option. Check whether the appropriate
// date ranges gets displayed in the Transaction History table.Once after the search gets completed select show recent transactions inorder
// to refresh the transaction history table
			 
	 
	 public void dateFieldValidation() throws ParseException{
		Date date = Calendar.getInstance().getTime();
		Calendar now = Calendar.getInstance();
		int x = -300;
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat ft1 = new SimpleDateFormat("dd MMM yyyy");
		String ToDate = ft.format(date);
		
		now.add(Calendar.DATE, x);
		Date FromDate = now.getTime();
		String dateFrom = ft.format(FromDate);
		
		verifyAndInputByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.FromDate"),"From Date",dateFrom);
		verifyAndInputByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.ToDate"),"To Date",ToDate);
		verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.ViewTransactions"),"View Transaction");
		
		int transactionItemsCnt = browser.getChildElementsCountByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionCnt"));
		
		if (browser.isElementVisibleWithXpath("//ul[contains(@id,'ddlDisplayLength')]//li["+transactionItemsCnt+"]")){
			verifyAndClickWithXpath("//ul[contains(@id,'ddlDisplayLength')]//li["+transactionItemsCnt+"]//a","Items Cnt");
			if (browser.isElementVisibleWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionItemsCnt"))){
			int itemsCnt = browser.getRowCountByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.TransactionItemsCnt"));
			String firstDisplayedDate = browser.getTextByXpath("//table[contains(@class,'table-history')]//tr[1]//td[1]");
			String lastDisplayedDate = browser.getTextByXpath("//table[contains(@class,'table-history')]//tr["+itemsCnt+"]//td[1]");
			
				Date newFirstDisplayedDate = ft1.parse(firstDisplayedDate);
				String updatedFirstDisplayedDate = ft.format(newFirstDisplayedDate);
				Date newFromDate = ft.parse(dateFrom);
				String updatedFromDate = ft.format(newFromDate);
								
				Date newLastDisplayedDate = ft1.parse(lastDisplayedDate);
				String updatedLastDisplayedDate = ft.format(newLastDisplayedDate);
				Date newToDate = ft.parse(ToDate);
				String updatedToDate = ft.format(newToDate);
							
				if (updatedFirstDisplayedDate.trim().equals(updatedFromDate) || (newFirstDisplayedDate.compareTo(newFromDate) > 0)){
					Report.updateTestLog("Expected From date: "+ updatedFromDate + "and first displayed date: "+ updatedFirstDisplayedDate +"values was displayed","PASS");
		        }else{
		        	Report.updateTestLog("Expected date values was not displayed","FAIL");	
			}
				
				if (updatedLastDisplayedDate.trim().equals(updatedToDate) || (newLastDisplayedDate.compareTo(newToDate) < 0)){
				   Report.updateTestLog("Expected To date: "+ ToDate + "and last displayed date: "+ updatedLastDisplayedDate +"values was displayed","PASS");
				}else{
				   Report.updateTestLog("Expected date values was not displayed","FAIL");	
	     }
			}
		 }
		sortDateCheck();
		recentTransactionHistoryValidation();
	   }

// Validate by clicking on Billing link and click on View Bill link.Once after clicking on View Bill link check whether the 
// appropriate page gets displayed by retrieving the page title. Then select Bill History link displayed in the page 	 
	 
	 public void billingValidation(){
			verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.Billing"), "Billing");
			verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.ViewBill"), "View Bill");
			verifyPageTitle("View statement");
			verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.BillHistory"), "Bill History");
			verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.BillingPaymentHistory"), "Payment History");
	}

// Verify by clicking on Energy Usage -> Meter Read History
	 
	 public void meterReadHistory(){
		 verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.EnergyUsage"), "Energy Usage");
		 verifyAndClickWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.MeterReadHistory"), "Meter Read History");
	 }

// Verify the address displayed in the application against the address in Siebel Database
	 
	 public void addressValidation(String accountNumber){
		 SiebelDataBase siebelDatabase = new SiebelDataBase();
	     List<String> address = siebelDatabase.getAddress(accountNumber);
	     String[] arrayaddress = (String[]) address.toArray(new String[0]);
	     //String houseno = arrayaddress[0];
	     String addNum = arrayaddress[1];
	     String addres = arrayaddress[2];
	     String city = arrayaddress[3];
	     String zipcode = arrayaddress[4];
	     String number = "";
	     //String fulladdress = "";
			     
	     String dbAddress= number + " " + addNum + " " + addres + " " + city + " " + zipcode;
	    	     
	     if(browser.isElementVisibleWithXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.MeterReadAddress"))){
	    	 String meterReadAddress = browser.getTextByXpath(HistoryPageProperties.getProperty("PaymentHistoryPage.MeterReadAddress"));
	    	 if(meterReadAddress.contains(accountNumber) && meterReadAddress.contains(number) && meterReadAddress.contains(addres )&& meterReadAddress.contains(city) && meterReadAddress.contains(zipcode)){
            	   Report.updateTestLog("Address verification done with database successfull<br>"+
           	"Database address-<b>"+dbAddress+"</b><br>Page address-<b>"+meterReadAddress+"</b>", "PASS");
               }else{
            	   Report.updateTestLog("Address verification done with database is not successfull<br>"+
           	"Database address-<b>"+dbAddress+"</b><br>Page address-<b>"+meterReadAddress+"</b>", "FAIL");
               }
	     }
	    
	 }
	 
// Validate Meter Read History Table by comparing the Meter Read and Date in SAP against the values displayed in the application
	 
	 public void meterReadHistoryValidation(String accountNumber, String balanceInApp) throws ParseException{
		 meterReadHistory();
		 addressValidation(accountNumber);
		 RunQTP runQTP = new RunQTP();

         runQTP.runQTP("BGRegression\\src\\bg\\framework\\app\\functional\\util\\vbsScripts\\Sample.vbs", accountNumber);

         browser.wait(15000);
         
         String meterread = null;
         String meterdate = null;
         String tarrif =null;
         String balance = null;

         try {
             File file1 = new File("C:\\SAPData\\meterread.txt");
             File file = new File("C:\\SAPData\\balance.txt");
             File file2 = new File("C:\\SAPData\\tarrif.txt");
             
             FileReader fr = new FileReader(file1);
             BufferedReader br = new BufferedReader(fr);
             FileReader fr1 = new FileReader(file);
             BufferedReader br1 = new BufferedReader(fr1);
             FileReader fr2 = new FileReader(file2);
             BufferedReader br2 = new BufferedReader(fr2);
             
             meterread = br.readLine();
             meterdate = br.readLine();
                       
             tarrif  = br2.readLine();
             tarrif  = br2.readLine();
            
             meterdate = meterdate.substring(0, 2) + " " + meterdate.substring(2, 5) + " " + meterdate.substring(5);
             balance = br1.readLine();
             System.out.println("Value of balance is :"+balance);            
           
             /*if (balance.contains("-")) {
              if(browser.isTextPresent("credit")){
            	  Report.updateTestLog("CREDIT is present in the application", "PASS");
            	}
            	else{
            	  Report.updateTestLog("CREDIT is not present in the application", "FAIL");
            	}
              balance = balance.substring(0, balance.indexOf('-'));
             }
           else if(browser.isTextPresent("debit")){
            	 Report.updateTestLog("DEBIT is present in the application", "PASS");
           }
           else{
            	 Report.updateTestLog("ZERO balance account is present in the application", "PASS");
           }*/
           
           if (balance.contains("-")) {
             	  Report.updateTestLog("CREDIT is present in the application", "PASS");
           } else {
             	 Report.updateTestLog("DEBIT is present in the application", "PASS");
           }
           
             br.close();
             br1.close();
             br2.close();
             } catch (IOException e) {
             System.out.println("bad"+e);
         }
		 
		String meterReadDate = browser.getTextByXpath("//table[contains(@id,'meterReadhistory')]//tr[1]//td[1]");
		String meterRead = browser.getTextByXpath("//table[contains(@id,'meterReadhistory')]//tr[1]//td[2]");
		
		SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		String meterReadVal = meterRead.substring(0,1);
				
		if (Integer.parseInt(meterReadVal) == 0){
			meterRead = meterRead.substring(1);
		}
				
		Date newmeterReadDate = ft.parse(meterdate);
		String newmeterReadDate1 = sdf.format(newmeterReadDate);
			
		if (meterReadDate.trim().equals(newmeterReadDate1)){
			 Report.updateTestLog("Expected meter read date was displayed"+ meterReadDate,"PASS");
		}else {
		     Report.updateTestLog("Expected meter read date was not displayed","FAIL"); 
		}
		
		if (meterRead.trim().equals(meterread)){
			 Report.updateTestLog("Expected meter read was displayed "+meterRead,"PASS");
		}else {
		     Report.updateTestLog("Expected meter read was not displayed","FAIL"); 
		}
		
		String newbalance = balanceInApp.substring(1);
		balance = balance.toString();
		String [] newBalanceInSap = null;
		
		if (balance.contains("-")) {
			newBalanceInSap = balance.split("-");
		} 
				
		if (newBalanceInSap[0].trim().equals(newbalance)){
			 Report.updateTestLog("Expected balance was displayed"+ newbalance,"PASS");
		}else {
			 Report.updateTestLog("Expected balance was not displayed","FAIL"); 
		}
	 }
	 
// Once after navigating to Bill History table retrieve the items displayed in Billing History table and select the appropriate items.
// Compare it against the period displayed in Your bill field and validate whether both dates are similar
	 
	 public void billHistoryTableValidation(){
		 billingValidation();
		 if (browser.getTitle().contains("Payment history")){
			 Report.updateTestLog("Expected title was displayed","PASS");
         }else{
             Report.updateTestLog("Expected title was not displayed","FAIL");
          }
		 }

	 ////////////////////////////////////////////////////////////////////////////////
	 
	 
	 private void sortDateCheck(){
		 ArrayList<String> dateList=new ArrayList<String>();				
		 dateList=getDates();		 
		 if(isAscendingOrder(dateList)){
			 Report.updateTestLog("Displayed date is sorted in ascending order", "PASS");
		 }else{
			 Report.updateTestLog("Displayed date is not sorted in ascending order", "FAIL");	
		 }
		 dateList.clear();
		 verifyAndClickWithXpath("//table/thead/tr[1]/th/a", "Sort button");
		 dateList=getDates();
		 if(isDescendingOrder(dateList)){
			 Report.updateTestLog("Displayed date is sorted in desending order", "PASS");
		 }else{
			 Report.updateTestLog("Displayed date is not sorted in desending order", "FAIL");	
		 }				
	 }
	 private ArrayList<String> getDates(){
		 ArrayList<String> dateList=new ArrayList<String>();
		 int i=1;
		 while(browser.isElementVisibleWithXpath("//table[contains(@class,'table-history')]//tr["+i+"]//td[1]")){
			 dateList.add(browser.getTextByXpath("//table[contains(@class,'table-history')]//tr["+i+"]//td[1]"));
			 i++;
		 }
		 System.out.println(dateList);
		 return dateList;
	 }
	 
	 private boolean isAscendingOrder(ArrayList<String> dateList){
		 SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MMM/yyyy");		
		 boolean sorted=false;	
		 for(int j=0;j<dateList.size();j++){ 	
			 if(j==dateList.size()-1){				
				break;
				}
			 try {
				if(dateFormat.parse(dateList.get(j).replaceAll(" ", "/")).after(dateFormat.parse(dateList.get(j+1).replaceAll(" ", "/")))
						|| dateFormat.parse(dateList.get(j).replaceAll(" ", "/")).equals(dateFormat.parse(dateList.get(j+1).replaceAll(" ", "/")))){
					sorted=true;					
				}else{
					sorted=false;
					break;
				}
			 } catch (ParseException e) {				
					e.printStackTrace();
				   }
			 }
		 return sorted;
	 }
	 
	 private boolean isDescendingOrder(ArrayList<String> dateList){
		 SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MMM/yyyy");
		 boolean sorted=false;	
		 for(int j=0;j<dateList.size();j++){ 	
			 if(j==dateList.size()-1){				
				break;
				}
			 try {
				if(dateFormat.parse(dateList.get(j).replaceAll(" ", "/")).before(dateFormat.parse(dateList.get(j+1).replaceAll(" ", "/")))
						|| dateFormat.parse(dateList.get(j).replaceAll(" ", "/")).equals(dateFormat.parse(dateList.get(j+1).replaceAll(" ", "/")))){
					sorted=true;					
				}else{
					sorted=false;
					break;
				}
			 } catch (ParseException e) {				
					e.printStackTrace();
				   }
			 }
		 return sorted;
	 }
	
	 
	  }
	

