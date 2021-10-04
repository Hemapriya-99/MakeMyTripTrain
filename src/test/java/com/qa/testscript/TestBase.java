package com.qa.testscript;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.pages.TrainReservationPages;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	WebDriver driver;
	TrainReservationPages trainReservationPages;
	JavascriptExecutor js;
	Properties prop;
	FileInputStream fileLoc;
	Actions act;
	SoftAssert sa;

	@Parameters({ "Browser", "Url" })
	@BeforeClass
	public void setUp(String Browser, String Url) throws InterruptedException, IOException {
		if (Browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (Browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		else if (Browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
		else if (Browser.equalsIgnoreCase("ie")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get(Url);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		trainReservationPages = new TrainReservationPages(driver);
		fileLoc=new FileInputStream("C:\\Users\\admin\\eclipse-TestingEEWorkspace\\MakeMyTripTrain\\src\\test\\java\\com\\qa\\testdata\\InputData.properties");
		prop=new Properties();
		prop.load(fileLoc);
		js = (JavascriptExecutor) driver;
		act = new Actions(driver);
		sa = new SoftAssert();
	}

	@AfterClass
	public void teardown() {
		driver.quit();
	}

	public void captureScreenshot(String tname) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File target = new File(System.getProperty("user.dir") + "/Screenshots/" + tname + ".png");
		FileUtils.copyFile(source, target);
		System.out.println("Screenshot Captured");
	}

	 public void login() throws InterruptedException {
		  trainReservationPages.getLoginField().click();
		  trainReservationPages.getEmailField().sendKeys(prop.getProperty("username"));
		  trainReservationPages.getContinueField().click(); 
		  Thread.sleep(2000); 
		  trainReservationPages.getPasswordField().sendKeys(prop.getProperty("password"));
		  Thread.sleep(2000); 
		  trainReservationPages.getSubmitLogin().click();
		  Thread.sleep(4000);
		  trainReservationPages.getClosePopup().click();
		  Thread.sleep(2000);
	  }
}
