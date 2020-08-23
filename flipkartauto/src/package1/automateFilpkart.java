package package1;



import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class automateFilpkart {
	public static WebDriver driver;
	@Test
	@Parameters({"nodeurl","browserName"})
public void testGrid(String nodeurl,String browserName) throws EncryptedDocumentException, IOException
	{
	//Browser Settings
	URL u =new URL(nodeurl);
	DesiredCapabilities dc =new DesiredCapabilities();
	dc.setBrowserName(browserName);
	WebDriver driver =new RemoteWebDriver(u, dc);
	driver.manage().window().maximize();
	driver.manage().deleteAllCookies();
	driver.manage().timeouts().pageLoadTimeout(40,TimeUnit.SECONDS);
	driver.manage().timeouts().implicitlyWait(25,TimeUnit.SECONDS);
	
	// Login into flipkart by reading user name and password from the spredsheet
	driver.get("http://www.flipkart.com");
	Reporter.log("brower  opened",true);
	Workbook wk = WorkbookFactory.create(new File("./Testdata/Testdatafilpkart1.xlsx"));
	String un = wk.getSheet("demo1").getRow(0).getCell(0).toString();
	String pw = wk.getSheet("demo1").getRow(0).getCell(1).toString();
	driver.findElement(By.xpath("//*[@class='_2zrpKA _1dBPDZ']")).sendKeys(un);
	driver.findElement(By.xpath("//*[@class='_2zrpKA _3v41xv _1dBPDZ']")).sendKeys(pw);
	driver.findElement(By.xpath("//*[@class='_2AkmmA _1LctnI _7UHT_c']")).click();
	Reporter.log("brower  login",true);
	
	//Searched Camera product
	WebDriverWait wait =new WebDriverWait(driver,120);
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@title='Search for products, brands and more']")));
	driver.findElement(By.xpath("//*[@class='LM6RPg']")).sendKeys("Camera");
	driver.findElement(By.xpath("//*[@class='vh79eN']")).sendKeys(Keys.ENTER);
	Reporter.log("searched",true);
	
	//Identified the location of the product & click
	WebElement sonycam = driver.findElement(By.xpath("//*[.='Sony CyberShot DSC-W800/BC IN5']"));
	Point loc = sonycam.getLocation();
	int x = loc.getX();
	int y = loc.getY();
	Reporter.log("x axis" +x);
	Reporter.log("y axis" +y);
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("scrollBy(0,"+y+")");
	Reporter.log("srcrolled",true);
	driver.findElement(By.xpath("(//*[text()='Effective Pixels: 20.1 MP'])[1]")).click();
	
	//Transfer the control from parent to child window
	Set<String> windowhandle = driver.getWindowHandles();
    String window1 = (String) windowhandle.toArray()[0];
    String window2=(String) windowhandle.toArray()[1];
    driver.switchTo().window(window2);
    
    //Storing the product name,description,price for verification on checkout screen
    String product_namewithdescription = driver.findElement(By.xpath("//*[@class='_35KyD6']")).getText();
    Reporter.log(product_namewithdescription,true);
    String price = driver.findElement(By.xpath("//*[@class='_1vC4OE _3qQ9m1' and text()='₹7,199']")).getText();
    Reporter.log(price,true);
    
    //Adding the product to the cart
	driver.findElement(By.xpath("//*[@class='_2AkmmA _2Npkh4 _2MWPVK']")).click();
	Reporter.log("click addto cart",true);
	
	//Assert setting for verification of product description & price
	String productaddcart = driver.findElement(By.xpath("//*[text()='Sony CyberShot DSC-W800/BC IN5']")).getText();
	Reporter.log(productaddcart,true);
	String Total_price = driver.findElement(By.xpath("(//*[text()=' ₹7,199'])[2]")).getText();
	Reporter.log(Total_price,true);
	Assert.assertEquals(product_namewithdescription, productaddcart);
    Assert.assertEquals(Total_price, price);
    
    //place an order & Select the payment method
    driver.findElement(By.xpath("//*[@class='_2AkmmA iwYpF9 _7UHT_c']")).click();
    driver.findElement(By.xpath("//*[@class='_2AkmmA _2Q4i61 _7UHT_c']")).click();
    driver.findElement(By.xpath("//*[text()='Net Banking']")).click();
    driver.findElement(By.xpath("//*[text()='ICICI Bank']")).click();
    driver.findElement(By.xpath("//*[text()='PAY ₹7,199']")).click();
    
    //Transfer the control to parent window for logout screen
    driver.switchTo().window(window1);
    WebElement Accountdropdown = driver.findElement(By.xpath("(//*[@class='_14jSvk'])[1]"));
    Actions act =new Actions(driver);
    act.moveToElement(Accountdropdown).perform();
    
    //logging out & quitting the browse
    WebElement logout = driver.findElement(By.xpath("//*[contains(text(),'Logout')]"));
    Actions act1 =new Actions(driver);
    act1.doubleClick(logout).perform();
	driver.quit();
}}



