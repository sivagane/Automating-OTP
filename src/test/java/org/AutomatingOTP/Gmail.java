package org.AutomatingOTP;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Gmail {

	public static final String ACCON_SID ="AC41edd56fb639507fac44b8c70ebd4ca6";
	public static final String AUTH_ID="33d4e505994bcad826f92c066d0d9e49";
	public static WebDriver driver;
	public static void main(String[] args) {


		WebDriverManager.chromedriver().setup();
		driver= new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://accounts.google.com/signup");
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		driver.findElement(By.id("firstName")).sendKeys("Ram");
		driver.findElement(By.id("lastName")).sendKeys("Kumar");
		driver.findElement(By.id("username")).sendKeys("naveenanimatio");
		driver.findElement(By.name("Passwd")).sendKeys("Test@1234");
		driver.findElement(By.name("ConfirmPasswd")).sendKeys("Test@1234");
		driver.findElement(By.xpath("//span[@class='RveJvd snByac']")).click();
		driver.findElement(By.xpath("//div[@class='ry3kXd YuHtjc']")).click();
		
		WebElement CountryBtn = driver.findElement(By.xpath("(//div[@class='MocG8c aCjZuc LMgvRb'])[224]"));
		JavascriptExecutor js=(JavascriptExecutor)driver;
	    js.executeScript("arguments[0].scrollIntoView(true)",CountryBtn);
	    CountryBtn.click();
		driver.findElement(By.xpath("//input[@class='whsOnd zHQkBf']")).sendKeys("2057408496");
		driver.findElement(By.xpath("//span[text()='Next']")).click();
		
		Twilio.init(ACCON_SID, AUTH_ID);
		String smsBody = getMessage();
		String otpNumber = smsBody.replaceAll("[^-?0-9]+", " ");
		
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