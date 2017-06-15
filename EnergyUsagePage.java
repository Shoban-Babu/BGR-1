package bg.framework.app.functional.page.reFactoring;

import java.util.ArrayList;
import java.util.Properties;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

public class EnergyUsagePage extends BasePage {
	private final static String File_AccPage = "resources/ReFactoring/Energy Usage.properties";
	private static Properties pageProperties = new PropertyLoader(File_AccPage).load();
	public void navigateToEnergyUsage() {
		verifyAndClickWithXpath(pageProperties.getProperty("EnergyUsage.EnergyUsage"), "Energy Usage");


	}
	public void verifyLinks() {
		verifyPageTitle("Energy usage");
		if(ApplicationConfig.BRAND.equalsIgnoreCase("BG")){
			ArrayList<String> link = new ArrayList<String>();
			link.add("EnergyUsage.MeterReadHistory");
			link.add("EnergyUsage.Compareyourusage");
			link.add("EnergyUsage.Predictnextbill");
			link.add("EnergyUsage.viewallbills");
			String s = link.toString();
			for (String i : link) {
				System.out.println(i);
				verifyLink(i);
			}
		}
	}
	private void verifyLink(String link) {
		String linkName = link.substring(link.indexOf('.') + 1, link.length());
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty(link))) {
			Report.updateTestLog(linkName + " Link Exist", "PASS");
		} 
		else {
			Report.updateTestLog(linkName + " Link not Exist", "FAIL");
		}


	}
	public void viewallbills() {
		verifyAndClickWithXpath(pageProperties.getProperty("EnergyUsage.viewallbills"), "View All Bills");

	}

}
