package bg.framework.app.functional.page.reFactoring;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

public class BookingCompletePage extends BasePage {
	private final static String strFile = "resources/ReFactoring/AccountOverview.properties";
    private static Properties PageProperties = new PropertyLoader(strFile).load();
    


public void verifyContactDetailsPage()
{
	verifyIsTextPresent(PageProperties.getProperty("ContactDetails.NotificationText"), "Contact Details notification Text");
	verifyIsTextPresent(PageProperties.getProperty("ContactDetails.YourContactText"), "Contact Details Page Text");
}

@SuppressWarnings("static-access")
public void checkDateFormat()
{
	Calendar currentDate = Calendar.getInstance();
	SimpleDateFormat oDateFormat = new SimpleDateFormat("MMM yyyy");
	for (int i = 0; i <= 6; i++) {
		String oStrDate = "";
		currentDate.add(Calendar.DATE, i);
		oStrDate = oDateFormat.format(currentDate.getTime());
		String dayName[] = new DateFormatSymbols().getWeekdays();
		String FinalDate = dayName[currentDate.get(currentDate.DAY_OF_WEEK)]
				.toString().subSequence(0, 3)
				+ " "
				+ currentDate.get(currentDate.DAY_OF_MONTH)
				+ " "
				+ oStrDate;

		if (browser.isTextPresent(FinalDate)) {
			Report.updateTestLog(
					"The date format is verified and the date " + FinalDate
							+ " is present", "PASS");
			break;
		} else {
			if (i == 6) {
				Report.updateTestLog(
						"The date format is verified and the date is not present",
						"FAIL");
			}
		}

	}
}

public void navigateToReviewWithPriority(int intFlag)
{
	browser.wait(15000);
	verifyIsElementVisibleWithXpath(PageProperties.getProperty("Review.PopUpId"),"Navigated to Book an engineer to fix a fault  page");
	verifyIsElementVisibleWithXpath(PageProperties.getProperty("Review.PopUpId"), "Popup page");
	
	verifyIsTextPresent(PageProperties.getProperty("IdentifyFault.HeaderText"));

	if (intFlag == 1) {
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("IdentifyFault.PopUpContinue"),"Continue button");
		verifyAndClickWithXpath(PageProperties.getProperty("IdentifyFault.PopUpContinue"),"Continue Identify Fault");
	}

	browser.wait(1000);
	
	verifyIsElementVisibleWithXpath(PageProperties.getProperty("Priority.No"), "No priority radio button");
	verifyIsElementVisibleWithXpath(PageProperties.getProperty("Priority.Yes"), "priority radio button");
	verifyAndClickWithXpath(PageProperties.getProperty("Priority.Yes"), "priority radio button");
	verifyIsElementVisibleWithXpath(PageProperties.getProperty("IdentifyFault.Continue"),"Continue button");
	verifyAndClickWithXpath(PageProperties.getProperty("IdentifyFault.Continue"),"Continue");
}



}
