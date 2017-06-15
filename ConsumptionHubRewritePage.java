package bg.framework.app.functional.page.reFactoring;

import java.util.ArrayList;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Ordering;

import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.RobotSendKeys;
import bg.framework.common.functional.UIDriver;
import bg.framework.common.functional.WebDriverProvider;

public class ConsumptionHubRewritePage extends BasePage {
	private final static String File_Name= "resources/reFactoring/ConsumptionHubRewrite.properties";
	private static Properties PageProperties =new PropertyLoader(File_Name).load();
	
	public void navigateToUKEnergyUsage()
	{
		browser.open("https://10.224.70.32/EnergyUsage/Usage_Comparison_Anonymous/");
	}
	
	public void navigateToCityComparison()
	{
		browser.open("https://10.224.70.32/EnergyUsage/Usage_Comparison_City_Level/");
		Report.updateTestLog("Navigated to City usage page", "Pass");
	}
	
	public void navigateToCompareEnergyUsage()
	{
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.CompareEnergyUsage"), "Compare Energy Usage Link");
		
	}
	
	public void updateLocationBy(String filter,String fuelType)
	{
		verifyAndSelectDropDownBox(PageProperties.getProperty("ConsumptionHub.EnergyType"), "Energy Type ", fuelType);
		if(filter=="PostCode")
		{
			verifyAndInputById(PageProperties.getProperty("ConsumptionHub.PostCode"), "PostCode","TW18 3HE" );
		}
		else if(filter=="Location")
		{
		verifyAndInputById(PageProperties.getProperty("ConsumptionHub.Location"), "Country Name","London" );
		}
		browser.wait(5000);
		//verifyAndClick(PageProperties.getProperty("ConsumptionHub.UpdateLocation"), "Update Location");
		//verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateLocation"), "Update Location");
		browser.clickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateLocation"));
		browser.wait(20000);
		//waitUntilElementVisibleByXpath(PageProperties.getProperty("ConsumptionHub.UsageSummary"),"Average Usage Summary");
		browser.wait(10000);
		verifyIsElementVisibleById(PageProperties.getProperty("ConsumptionHub.AverageUsage"), "Average Usage");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("ConsumptionHub.MinAvg"), "Min Average Usage");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("ConsumptionHub.MaxAvg"), "Max Average Usage");
	}
	
	
	public void updateLocationErrorMessage(String filter,String fuelType)
	{
		verifyAndSelectDropDownBox(PageProperties.getProperty("ConsumptionHub.Location"), "Energy Type", fuelType);
		if(filter=="PostCode")
		{
			verifyAndInputById(PageProperties.getProperty("ConsumptionHub.PostCode"), "PostCode","qwerqwe" );
			verifyAndClick(PageProperties.getProperty("ConsumptionHub.UpdateLocation"), "Update Location");
			verifyIsTextPresent("postcode you have entered is invalid", "Invalid PostCode");
		}
		else if(filter=="Location")
		{
			verifyAndInputById(PageProperties.getProperty("ConsumptionHub.Location"), "Country Name","rerff" );
			verifyAndClick(PageProperties.getProperty("ConsumptionHub.UpdateLocation"), "Update Location");
			verifyIsTextPresent("location you have entered is invalid", "Invalid PostCode");	
		}
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.ukusageYourAccount"), "Your Account");
		verifyPageTitle("Login");
	}
	
	public void changeCities(String energyType)
	{
		//verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("ConsumptionHub.CityEnergyType"), "Energy Type", energyType);
		//cityResultsPageNavigation();
		String[] a=new String[20];
		ArrayList<String> s=new ArrayList<String>();
		for(int i=1;i<7;i++)
		{
			if(browser.isElementVisibleWithXpath((PageProperties.getProperty("ConsumptionHub.CityResults")).replace("NUMBER", ""+i)))
			{
				a[i]=browser.getTextByXpath((PageProperties.getProperty("ConsumptionHub.CityResults")).replace("NUMBER", ""+i));
				System.out.println("THe result cities is" + a[i]);
				verifyAndClick(a[i], ""+a[i]);
				
				if(i==1)
				{
					//cityCheckBoxVerification(a[i]);
					verifyAndSelectCheckBoxByXpath((PageProperties.getProperty("ConsumptionHub.Closetable")).replace("CITYNAME",a[i]), "Close From Table");
					verifyAndClick(a[i], ""+a[i]+" city ");
				}
				if(i==6)
				{
					verifyIsTextPresent("only select a maximum of 5 UK cities", "Error for more than 5 cities");
					verifyAndClick(PageProperties.getProperty("ConsumptionHub.ResetSelection"),"Reset Selection");
					/*if(browser.isElementVisibleWithXpath(PageProperties.getProperty("ConsumptionHub.AfterReset")))
					{
						Report.updateTestLog("The reset operation is done Successfully", "PASS");
					}
					else{
						Report.updateTestLog("The Reset Operation has failed", "FAIL");
					}*/
				}
				/*else{
				//waitUntilElementVisibleByXpath((PageProperties.getProperty("ConsumptionHub.CityUsageSummary")).replace("CITYNAME",a[i]),
					//	""+a[i]+" city's Usage Summary");
				verifyAndSelectCheckBoxByXpath((PageProperties.getProperty("ConsumptionHub.Closetable")).replace("CITYNAME",a[i]), "Close From Table");
				}*/
				
			}
		}
	}
	
	public void cityCheckBoxVerification(String city)
	{
		if(browser.isSelected(city))
		{
			verifyAndClick(city, "City"+city);
			if(!browser.isSelected(city))
			{
				Report.updateTestLog("The city"+city+" is unselected", "PASS");
			}
			else{
				Report.updateTestLog("The city"+city+" is not unselected", "FAIL");
			}
			verifyAndClick(city, "City "+city);
		}
	}
	
	public void cityResultsPageNavigation()
	{
		verifyAndClickWithLinkText("Help?", "Help Link Text");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("ConsumptionHub.HelpTextModalOverlay"), "Help Modal Overlay");
		//verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.CloseHelpOverlay"), "Close Overlay");
		RobotSendKeys.typeEscape();
		String[] cityNames={"d","Aberdeen","Derby","Hull","Oxford","Truro","York"};
		for(int i=1;i<8;i++)
		{
			if(browser.isElementVisibleWithXpath((PageProperties.getProperty("ConsumptionHub.PageNavigation")).replace("PAGE",""+i)))
			{
				verifyAndClickWithXpath((PageProperties.getProperty("ConsumptionHub.PageNavigation")).replace("PAGE",""+i), "Page "+i);
				verifyAndClick(cityNames[i], "City");
				System.out.println(cityNames[i]);
				if(browser.isSelected(cityNames[i])){
				waitUntilElementVisibleByXpath((PageProperties.getProperty("ConsumptionHub.CityUsageSummary")).replace("CITYNAME",cityNames[i]),
						""+cityNames[i]+" city's Usage Summary");
				}
			}
		}
		verifyIsTextPresent("only select a maximum of 5 UK cities", "Error for more than 5 cities");
	}
	
	
	
	public void multiCityOverlay()
	{
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.CityLocation"),"Search FOr City","weston");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.CitySearchButton"), "Go");
		//waitUntilElementVisibleByXpath(PageProperties.getProperty("ConsumptionHub.notUniqueOverlay"), "Overlay");
		//verifyIsElementVisibleWithXpath(PageProperties.getProperty("ConsumptionHub.notUniqueOverlay"), "Overlay for Multi City");
		verifyAndClickWithLinkText(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.1stmultiCity")), "Multi City1");
		if(browser.isSelectedByXpath("//*[@id='Bath']"))
		{
			Report.updateTestLog("The Corresponding City in the League table is selected","PASS");
		}
		else{
			Report.updateTestLog("The Corresponding City in the League table is not selected","FAIL");
		}
		
	}
	
	public void enterCityDetails(){
		verifyAndInputById(PageProperties.getProperty("ConsumptionHub.Location"), "Country Name","Bath" );
		//verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.CityBath"), "Bath checkbox");
		verifyAndClick(PageProperties.getProperty("ConsumptionHub.GoButton"), "Enter City search button");
		browser.wait(10000);
		if(browser.isSelectedByXpath(PageProperties.getProperty("ConsumptionHub.CityBath"))){
			Report.updateTestLog("The Bath Checkbox has been checked", "Pass");
		}
		else{
			Report.updateTestLog("The Bath Checkbox is not checked", "Fail");
		}
	}
	
	public void verifyFuelToggle(){
		verifyAndSelectDropDownBox(PageProperties.getProperty("ConsumptionHub.EnergyType"), "Energy Type", "Electricity");
		if(!browser.isSelectedByXpath(PageProperties.getProperty("ConsumptionHub.CityBath"))){
			Report.updateTestLog("The Bath Checkbox has not been checked", "Pass");
		}
		else{
			Report.updateTestLog("The Bath Checkbox is checked", "Fail");
		}
	}
	
	public void verifyCitySelection(){
		for(int i=1;i<7;i++){
			verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.CityCheckSelect").replace("CityCheck", ""+i), "Checkbox");
			browser.wait(10000);
		}
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")).contains("you can only select a maximum of 5 UK cities")){
			Report.updateTestLog("You can Select only maximum of 5 UK cities Error message is displayed", "Pass");
		}
		else{
			Report.updateTestLog("The Expected error message not displayed for selecting more than 5 cities", "Fail");
		}
	}
	
	public void verifySortOrderCity(String SortingOrder){
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.Ascending"),"Sort button" );
		String Temp1="",Temp2="";
		String[] AlphabetOrderAscending = {" ","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String[] AlphabetOrderDescending = {"Z","Y","X","W","V","U","T","S","R","Q","P","O","N","M","L","K","J","I","H","G","F","E","D","C","B","A"," "};
		boolean flag = true;
		for(int i=1;i<10;i++){
			Temp1 = browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabelSelect").replace("CityLabel", ""+i));
			Temp2 = browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabelSelect").replace("CityLabel", ""+(i+1)));
			if(SortingOrder == "Ascending"){
				flag = sortStrings(Temp1,Temp2,AlphabetOrderAscending);
			}
			else{
				flag = sortStrings(Temp1,Temp2,AlphabetOrderDescending);
			}
			if(flag == false){
				break;
			}
		}
		if(flag == false){
			Report.updateTestLog("The cities "+Temp1+" and "+Temp2+" are not in the "+SortingOrder+" order", "Fail");
		}
		else{
			Report.updateTestLog("All the cities are in "+SortingOrder+" order when sorted with "+SortingOrder, "Pass");
		}
	}
	
	public boolean sortStrings(String Temp1,String Temp2,String[] AlphabetOrderAscending){
		boolean flag = true;
		
		String Temp1Char, Temp2Char;
		int Temp1Value = 0,Temp2Value= 0;
		for(int i=0;i<Temp1.length();i++){
			Temp1Char = ""+Temp1.charAt(i);
			Temp2Char = ""+Temp2.charAt(i);
			if(Temp1Char.equalsIgnoreCase(Temp2Char)){
				continue;
			}
			else{
				for(int j=0;j<27;j++){
					if(Temp1Char.equalsIgnoreCase(AlphabetOrderAscending[j])){
						Temp1Value = j;
					}
					if(Temp2Char.equalsIgnoreCase(AlphabetOrderAscending[j])){
						Temp2Value = j;
					}
				}
				if(Temp1Value < Temp2Value){
					flag=true;
					break;
				}
				else{
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	
	public static void main(String args[]){
		String ggg = "Abcd";
		System.out.println("Feel    : "+ggg.charAt(0));
	}
	
	public void selectAccount(String AccountNumber){
		ArrayList<String> accountList = new ArrayList<String>();   
		if(browser.isElementVisible(PageProperties.getProperty("ConsumptionHub.SelectAccount")))
    	{
    		 accountList=(browser.getFromDropBox("id", PageProperties.getProperty("ConsumptionHub.SelectAccount")));
    	}
		for(String s:accountList)
    	{
    		if(s.contains(AccountNumber))
    		{
    			verifyAndSelectDropDownBox(PageProperties.getProperty("ConsumptionHub.SelectAccount"),"AccountDetails"+s,s);
    			break;
    		}
    	}
		
	}
	
	public void verifyErrorMessageNewCustomer(){
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")).equalsIgnoreCase("Sorry, we don't have enough data to provide an accurate comparison with all filters applied. Please uncheck one or more filters.")){
			Report.updateTestLog("Error Message displayed :"+browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")), "Pass");
		}
		else{
			Report.updateTestLog("The error text displayed is "+browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")), "Fail");
		}
	}
	
	public void logout(){
		browser.clickWithLinkText("Log out");
	}
	
	public void verifyPostCodeLogic(){
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "tw18 3he");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(15000);
		browser.wait(15000);
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabel")).contains("TW18 3")){
			Report.updateTestLog("The Postcode found in application page", "Pass");
		}
		else{
			Report.updateTestLog("The Postcode not found in the application page", "Fail");
		}
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "tw18 3");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(15000);
		browser.wait(15000);
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabel")).contains("TW18 3")){
			Report.updateTestLog("The Postcode found in application page", "Pass");
		}
		else{
			Report.updateTestLog("The Postcode not found in the application page", "Fail");
		}
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "tw18");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(15000);
		browser.wait(15000);
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabel")).contains("TW18")){
			Report.updateTestLog("The Postcode found in application page", "Pass");
		}
		else{
			Report.updateTestLog("The Postcode not found in the application page", "Fail");
		}
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "tw");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(15000);
		browser.wait(15000);
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabel")).contains("TW")){
			Report.updateTestLog("The Postcode found in application page", "Pass");
		}
		else{
			Report.updateTestLog("The Postcode not found in the application page", "Fail");
		}
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "t");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(15000);
		browser.wait(15000);
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")).contains("The postcode you have entered is invalid, please check and try again.")){
			Report.updateTestLog("The Postcode invalid error message is displayed in the application page", "Pass");
		}
		else{
			Report.updateTestLog("The Postcode invalid error message is not displayed in the application page", "Fail");
		}

	}
	
	
	public void enterRefineComparisonDetails(String Property,String Bedroom,String occupants,String AgeofProperty){
		int[][] InitialValue = new int[10][10];
		int[][] FinalValue = new int[10][10];
		//To click on the expand button
		browser.wait(15000);
		String jquery = "$('.accordion-head').trigger('click');";
		browser.executeScript(jquery);
		browser.wait(5000);
		//Get Initial Usage Values
		InitialValue = getEnergyUsage();
		//Enter details to refine search
		RefineDetails(Property,Bedroom,occupants,AgeofProperty);
		browser.wait(15000);
		browser.wait(15000);
		browser.wait(15000);
		browser.wait(15000);
		/*//To click on the expand button
		browser.wait(15000);
		jquery = "$('.accordion-head').trigger('click');";
		browser.executeScript(jquery);
		browser.wait(5000);*/
		//Get Values after refined search
		FinalValue = getEnergyUsage();
		//Compare Before and after search values
		compareConsumptionValues(InitialValue,FinalValue);
	}
	
	
	
	public void compareConsumptionValues(int[][] InitialValue,int[][] FinalValue){
		for(int i=1;i<5;i++){
			for(int j=2;j<5;j++){
				if(i == 3){
					if(InitialValue[i][j] == FinalValue[i][j]){
						Report.updateTestLog("For UserType "+
						browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableValues").replace("row", ""+i).replace("col","1")) + " and "+
						browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableHeader").replace("col",""+j))+
						" the Default Value is "+InitialValue[i][j]+" and the Updated Value is "+FinalValue[i][j]
						,"Pass");
					}
					else{
						Report.updateTestLog("For UserType "+
						browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableValues").replace("row", ""+i).replace("col","1")) + " and "+
						browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableHeader").replace("col",""+j))+
						" the Default Value is "+InitialValue[i][j]+" and the Updated Value is "+FinalValue[i][j]
						,"Fail");
					}
				}
				else if(InitialValue[i][j] != FinalValue[i][j]){
					Report.updateTestLog("For UserType "+
					browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableValues").replace("row", ""+i).replace("col","1")) + " and "+
					browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableHeader").replace("col",""+j))+
					" the Default Value is "+InitialValue[i][j]+" and the Updated Value is "+FinalValue[i][j]
					,"Pass");
				}
				else{
					Report.updateTestLog("For UserType "+
					browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableValues").replace("row", ""+i).replace("col","1")) + " and "+
					browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableHeader").replace("col",""+j))+
					" the Default Value is "+InitialValue[i][j]+" and the Updated Value is "+FinalValue[i][j]
					,"Fail");
				}
			}
		}
	}
	
	public void RefineDetails(String PropertyType,String Bedroom,String occupants,String AgeofProperty){
		//Enter Property
		if(!PropertyType.isEmpty()){
			verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertyType"), "Property type");
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertySelect"), "Property Select", PropertyType);
		}
		//Enter Bedroom
		if(!Bedroom.isEmpty()){
			verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofBedRooms"), "Number of Bedroom");
			verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub."+Bedroom),Bedroom);
		}
		//Enter Occupants
		if(!occupants.isEmpty()){
			verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofOccupants"), "Number of occupants");
			verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub."+occupants),occupants);
		}
		//Enter Age of property
		if(!AgeofProperty.isEmpty()){
			verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.AgeofProperty"), "Property Age Checkbox");
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("ConsumptionHub.SelectAgeofProperty"), "Age of property", AgeofProperty);
		}
		//Click update details
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateComparison"), "Update Button");
		
	}
	
	public int[][] getEnergyUsage(){
		int[][] temp = new int[10][10];
		for(int i=1;i<5;i++){
			for(int j=2;j<5;j++){
				temp[i][j] = Integer.parseInt(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.InformationTableValues").replace("row", ""+i).replace("col",""+j)));
				System.out.println(temp[i][j]);
			}
		}
		return temp;
	}
	
	public void verifyConsumptionComparisonBGSAccount(){
		if(!browser.isElementVisibleWithXpath(PageProperties.getProperty("ConsumptionHub.CompareEnergyUsage"))){
			Report.updateTestLog("Consumption comparison not available for BGS account", "Pass");
		}
		else{
			Report.updateTestLog("Consumption comparison link available for BGS account", "Fail");
		}
	}
	
	public void verifyComparisonDetails(){
		int[][] InitialValue = new int[10][10];
		int[][] FinalValue = new int[10][10];
		//To click on the expand button
		browser.wait(5000);
		String jquery = "$('.accordion-head').trigger('click');";
		browser.executeScript(jquery);
		browser.wait(5000);
		//Get Initial Usage Values
		InitialValue = getEnergyUsage();
		RefineDetails("Flat / Maisonette","","","");
		//To click on the expand button
		browser.wait(15000);
		/*jquery = "$('.accordion-head').trigger('click');";
		browser.executeScript(jquery);*/
		browser.wait(15000);
		FinalValue = getEnergyUsage();
		compareConsumptionValues(InitialValue,FinalValue);
	}
	
	
	public void verifyUpdatePremiseData(){
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.SaveLink"), "Save Link");
		browser.wait(3000);
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.SaveChanges"), "SaveChanges ");
	}
	
	public String getSelectedValueFromPropertyDropdown(){
		String SelectedValue ="";
		if(browser.isTextVisible("Flat / Maisonette")){
			SelectedValue = "Flat / Maisonette";
			
		}
		if(browser.isTextVisible("Terraced")){
			SelectedValue = "Terraced";
		}
		if(browser.isTextVisible("Detached")){
			SelectedValue = "Detached";
		}
		if(browser.isTextVisible("Semi-detached")){
			SelectedValue = "Semi-detached";
		}
		if(browser.isTextVisible("Bungalow")){
			SelectedValue = "Bungalow";
		}
		if(browser.isTextVisible("Premise Unknown")){
			SelectedValue = "Premise Unknown";
		}
		return SelectedValue;
	}
	
	
	public void verifySavedData(){
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertyType"), "Property type");
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofBedRooms"), "Number of Bedroom");
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofOccupants"), "Number of occupants");
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.AgeofProperty"), "Property Age Checkbox");
		
		
		if(browser.getAttributeByXpath(PageProperties.getProperty("ConsumptionHub.BedroomSelect2"), "value").equalsIgnoreCase("2") &&
			browser.getAttributeByXpath(PageProperties.getProperty("ConsumptionHub.Occupants3"), "value").equalsIgnoreCase("003"))
		{
			Report.updateTestLog("All the Saved Data Have been updated", "Pass");
		}
		else{
			Report.updateTestLog("All the Saved Data Have not been updated", "Fail");
		}
	}
	
	public void verifyNewSavedData(){
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertyType"), "Property type");
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofBedRooms"), "Number of Bedroom");
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofOccupants"), "Number of occupants");
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.AgeofProperty"), "Property Age Checkbox");
		if(browser.getAttributeByXpath(PageProperties.getProperty("ConsumptionHub.BedroomSelect2"), "value").equalsIgnoreCase("2") &&
			browser.getAttributeByXpath(PageProperties.getProperty("ConsumptionHub.Occupants3"), "value").equalsIgnoreCase("003"))
		{
			Report.updateTestLog("All the Saved Data Have been updated", "Pass");
		}
		else{
			Report.updateTestLog("All the Saved Data Have not been updated", "Fail");
		}
	}
	
	public void EnterCityDetails(){
		String[] Age = {"Pre 1919","1919 - 1944","1945 - 1976","Post 1976"};
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterCity"), "Nottingham City", "Nottingham");
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "", "");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(15000);
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabel")).equalsIgnoreCase("Nottingham")){
			Report.updateTestLog("The Consumption Comparison is showed for Nottingham city", "Pass");
		}
		else{
			Report.updateTestLog("The Consumption Comparison is not showed for Nottingham city", "Fail");
		}
		
	}
	
	
	public void EnterPostCodeDetails(){
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "tw18 3he");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(3000);
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.CityLabel")).contains("TW18")){
			Report.updateTestLog("The Consumption Comparison is showed for tw18 3he", "Pass");
		}
		else{
			Report.updateTestLog("The Consumption Comparison is not showed for tw18 3he", "Fail");
		}
	}
	
	public void verifyIncorrectPostcode(){
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "llllll");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		browser.wait(3000);
		//verification pending
	}
	
	public void enterRefineDetails(){
		//Enter Property
		//verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertyType"), "Property type");
		//verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertySelect"), "Property Select", "Flat / Maisonette");
		//Enter Bedroom
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofBedRooms"), "Number of Bedroom");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.BedroomSelect2"),"BedroomSelect2");
		//Enter Occupants
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofOccupants"), "Number of occupants");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.Occupants3"),"Occupants3");
		//Enter Age of property
		//verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.AgeofProperty"), "Property Age Checkbox");
		//verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("ConsumptionHub.SelectAgeofProperty"), "Age of property", "Post 1976");
	}
	
	public void enterNewRefineDetails(){
		//Enter Property
		//verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertyType"), "Property type");
		//verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("ConsumptionHub.PropertySelect"), "Property Select", "Detached");
		//Enter Bedroom
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofBedRooms"), "Number of Bedroom");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.BedroomSelect2"),"BedroomSelect2");
		//Enter Occupants
		verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.NumberofOccupants"), "Number of occupants");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.Occupants3"),"Occupants3");
		//Enter Age of property
		//verifyAndSelectCheckBoxByXpath(PageProperties.getProperty("ConsumptionHub.AgeofProperty"), "Property Age Checkbox");
		//verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("ConsumptionHub.SelectAgeofProperty"), "Age of property", "Post 1976");
	}
	
	public void verifySessionData(){
		browser.browserBack();
		navigateToCompareEnergyUsage();
		verifySavedData();
	}
	
	public void verifyErrorMessage(){
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterCity"), "Nottingham City", "Nottingham");
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "tw18 3he");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
	}
	
	public void verifyUnderstandYourUsageData(String UserType){
		verifyAndClickWithLinkText(PageProperties.getProperty("ConsumptionHub.UnderstandYourUsage"), "Understand your usage link");
		
	}
	
	public void verifyPostcodeandCity(){
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterCity"), "Nottingham City", "");
		verifyAndInputByXpath(PageProperties.getProperty("ConsumptionHub.EnterPostCode"), "tw18 3he postcode", "");
		verifyAndClickWithXpath(PageProperties.getProperty("ConsumptionHub.UpdateConsumptionLocation"), "Update Location");
		
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")).equalsIgnoreCase("Please enter either a postcode or location to proceed")){
			Report.updateTestLog("Error Message displayed :"+browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")), "Pass");
		}
		else{
			Report.updateTestLog("The error text displayed is "+browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")), "Fail");
		}
	}
	
	public void verifyErrorTextExtremeValues(){
		RefineDetails("Flat / Maisonette","BedroomSelect4","Occupants6","Pre 1919");
		browser.wait(15000);
		
		if(browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")).equalsIgnoreCase("Sorry, we don't have enough data to provide an accurate comparison with all filters applied. Please uncheck one or more filters.")){
			Report.updateTestLog("Error Message displayed :"+browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")), "Pass");
		}
		else{
			Report.updateTestLog("The error text displayed is "+browser.getTextByXpath(PageProperties.getProperty("ConsumptionHub.ErrorMessage")), "Fail");
		}
	}
	
}
