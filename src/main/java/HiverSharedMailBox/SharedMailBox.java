package HiverSharedMailBox;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class SharedMailBox {
	public static WebDriver driver;

	By emailId = By.xpath("//input[@type='email']");
	By nextEmailIdButton = By.xpath("//div[@class='VfPpkd-RLmnJb']");
	By password = By.xpath("//input[@type='password']");
	By NextPasswordButton = By.xpath("//span[contains(text(),'Next')]");
	By hiverIcon = By.xpath("//*[@fill='#FFD123']");
	By adminPanel = By.xpath("//*[@data-linktype='admin_panel']");
	By autoResponder = By.xpath("//span[contains(text(),'Auto Responder')]");
	By enableIcon = By.xpath("//*[@for='checkbox-default-3']");
	By saveButton = By.xpath("//button[contains(text(),'Save')]");
	By toastMessage = By.xpath("//div[@role='alert'][contains(@class,'Toastify__toast')]");
	By textEditorIframe = By.xpath("//iframe[@class='cke_wysiwyg_frame cke_reset']");

	public static void implicitWait(int time) {

		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);

	}

	public static void explicitWait(WebElement element, int time) {

		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(ExpectedConditions.elementToBeClickable(element));

	}

	@BeforeMethod
	public void setUpAndLaunchBrowser() {
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "/src/test/java/Browser/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		// options.addExtensions(new File(HiverExtensions.liveExtensionPath));
		options.addExtensions(new File(System.getProperty("user.dir") + "/src/test/java/Browser/hiver.crx"));

		driver = new ChromeDriver(options);
		/* Navigate to Gmail */
		// driver.get("https://accounts.google.com/");
		driver.get(
				"https://accounts.google.com/signin/oauth/oauthchooseaccount?client_id=717762328687-iludtf96g1hinl76e4lc1b9a82g457nn.apps.googleusercontent.com&scope=profile%20email&redirect_uri=https%3A%2F%2Fstackauth.com%2Fauth%2Foauth2%2Fgoogle&state=%7B%22sid%22%3A1%2C%22st%22%3A%2259%3A3%3ABBC%2C16%3Ad6d43be47e814b98%2C10%3A1591608975%2C16%3Afbe7e8befa57ecde%2Cff07eae6cb92814ccc53330ba3ad167d91b66afc6cf797a2b05e4e949aa68bcd%22%2C%22cdl%22%3Anull%2C%22cid%22%3A%22717762328687-iludtf96g1hinl76e4lc1b9a82g457nn.apps.googleusercontent.com%22%2C%22k%22%3A%22Google%22%2C%22ses%22%3A%2296fe9c384acc45fc91bc60a78b8cda25%22%7D&response_type=code&o2v=1&as=sqXj6IoZ_vrDuD8djofpog&flowName=GeneralOAuthFlow");

		driver.manage().window().maximize();
	}

	@Test
	@Parameters({ "gmailId", "gmailPassword", "message" })
	public void sharedMailBoxTest(String gmailId, String gmailPassword, String message) {

		/*
		 * login with the account which used for signing up for Hiver
		 */
		implicitWait(15);
		driver.findElement(emailId).sendKeys(gmailId);
		driver.findElement(nextEmailIdButton).click();
		driver.findElement(password).sendKeys(gmailPassword);
		driver.findElement(NextPasswordButton).click();
		/*
		 * Once logged in to gmail, login to Hiver by clicking on Hiver icon on
		 * top right corner in Gmail
		 */
		driver.findElement(hiverIcon).click();

		/*
		 * On successfully logged in to hiver, Click on Hiver settings icon (ie,
		 * yellow gear icon) and select Admin Panel
		 */
		driver.findElement(adminPanel).click();

		/*
		 * Once Dashboard is loaded, Click on Shared Mailbox from the left (2nd
		 * icon on left)
		 */

		ArrayList<WebElement> listCircle = (ArrayList<WebElement>) driver.findElements(By.xpath("//*[@id='circle']"));
		listCircle.get(1).click();

		/*
		 * Click on shared mailbox which you have already created from the list
		 */

		ArrayList<WebElement> listSharedEmailList = (ArrayList<WebElement>) driver
				.findElements(By.xpath("//span[contains(text(),'shared')]"));
		listSharedEmailList.get(1).click();

		/* Select Autoresponder option */

		driver.findElement(autoResponder).click();

		/* Click on enable icon from right side, add a message and save it */

		if (!driver.findElement(enableIcon).isEnabled()) {
			driver.findElement(enableIcon).click();
		}

		driver.switchTo().frame(driver.findElement(textEditorIframe));
		if (driver.findElement(By.cssSelector("body")).isEnabled()) {
			driver.findElement(By.cssSelector("body")).clear();
			driver.findElement(By.cssSelector("body")).sendKeys(message);
		}

		driver.switchTo().defaultContent();
		driver.findElement(saveButton).click();

		/* Verify the success message on toast and print it on console */

		String successToastMessage = driver.findElement(toastMessage).getText();
		Assert.assertEquals(successToastMessage, "Auto Responder text edited");
		System.out.println(successToastMessage);

	}

	@AfterMethod
	public void tearDown() {

		if (driver != null) {
			driver.quit();
		}

	}

}
