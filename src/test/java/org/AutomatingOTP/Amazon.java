package org.AutomatingOTP;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Amazon {

	public static final String ACCON_SID ="AC41edd56fb639507fac44b8c70ebd4ca6";
	public static final String AUTH_ID="33d4e505994bcad826f92c066d0d9e49";
	public static WebDriver driver;
	public static void main(String[] args) {

		WebDriverManager.chromedriver().setup();
		driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.amazon.in");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		Actions action= new Actions(driver);
		WebElement signinBtn = driver.findElement(By.id("nav-link-accountList"));
		action.moveToElement(signinBtn).build().perform();
		driver.findElement(By.xpath("(//a[text()='Start here.'])[1]")).click();
		driver.findElement(By.id("ap_customer_name")).sendKeys("Sivaganesh");
		driver.findElement(By.className("a-dropdown-prompt")).click();
		driver.findElement(By.xpath("//*[@id=\"auth-country-picker_212\"]")).click();
		driver.findElement(By.id("ap_phone_number")).sendKeys("2057408496");
		driver.findElement(By.name("password")).sendKeys("Test@1234");
        driver.findElement(By.id("continue")).click();
        
        Twilio.init(ACCON_SID, AUTH_ID);
        String smsBody = getMessage();
        System.out.println(smsBody);
        String OTPnumber = smsBody.replaceAll("[^-?0-9]+", " ");
        
        driver.findElement(By.id("auth-pv-enter-code")).sendKeys(OTPnumber);
        driver.findElement(By.id("auth-verify-button")).click();
        
      
		
	}
	  public static String getMessage() {
      	return getMessages().filter(m -> m.getDirection().compareTo(Message.Direction.INBOUND)==0)
      			.filter(m -> m.getTo().equals("+12057408496")).map(Message::getBody).findFirst()
      			.orElseThrow(IllegalStateException::new);
      }
      private static Stream<Message> getMessages() {
      	ResourceSet<Message> messages=Message.reader(ACCON_SID).read();  
      	return StreamSupport.stream(messages.spliterator(), false);
      }

}
