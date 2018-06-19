package porsche;

import java.util.Arrays;
import java.util.Iterator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Porsche {
	static final int OUTPUT_WIDTH = 100;
	
	
	public static double parseCurrency(String str) {
		return Double
				.parseDouble(str.substring(str.indexOf("$") + 1, str.lastIndexOf("0") + 1).replaceAll(",", "").trim());
	}

	public static void printStepResult(String stepText, double expected, double actual) {
		// System.out.println("---> Testing: " + stepText);
		// Below for loop just for a nice look - not needed. You can use the above line instead 
		for (int i = 0; i < stepText.length(); i+= OUTPUT_WIDTH) {
			if (i==0)
				System.out.println("> Testing: " + stepText.substring(i, stepText.length() > OUTPUT_WIDTH ? OUTPUT_WIDTH : stepText.length()));
			else
				System.out.println("           " + stepText.substring(i, stepText.length() > i + OUTPUT_WIDTH ? i + OUTPUT_WIDTH : stepText.length()));
		}
		
		if ((int) expected == (int) actual) {
			System.out.println(">> Pass");
		} else {
			System.out.println("## Fail");
			System.out.println("Expected: " + expected);
			System.out.println("Actual  : " + actual);
		}
		System.out.println();
	}

	public static void main(String[] args) throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		driver.get("https://www.porsche.com/usa/modelstart/");

		driver.findElement(By.xpath("//img[@alt='Porsche - 718']")).click();
		WebElement c718BaseRef = driver.findElement(By.id("m982120"));

		WebElement thisElement = c718BaseRef.findElement(By.xpath("//div[1]/div[2]/div[2]"));
		double basePrice = parseCurrency(thisElement.getText());

		System.out.println("[Step 4] Base Price is : " + basePrice);

		driver.findElement(By.xpath("//*[@id='m982120']/div[2]/div/a/span")).click();
		// c718BaseRef.findElement(By.xpath("//div[2]/div/a")).click();

		Iterator<String> iterator = driver.getWindowHandles().iterator();
		String subWindowHandler = null;
		while (iterator.hasNext()) {
			subWindowHandler = iterator.next();
		}
		driver.close();
		driver.switchTo().window(subWindowHandler); // switch to popup window

		// System.out.println(driver.getCurrentUrl());

		// Thread.sleep(1000);
		
		
		new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"s_price\"]/div[1]/div[1]/div[2]")));
		thisElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[1]/div[2]"));

		printStepResult("Step 6 - Verifyingy that Base price displayed on the page is same as the price before", basePrice,
				parseCurrency(thisElement.getText()));

		thisElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]"));
		printStepResult("Step 7 - Checking if equipment price is zero", 0, parseCurrency(thisElement.getText()));

		thisElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]"));
		double totalPrice = parseCurrency(thisElement.getText());
		thisElement = driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[3]/div[2]"));
		double deliveryPrice = parseCurrency(thisElement.getText());

		printStepResult(
				"Step 8 - Verifying that total price is the sum of base price + Delivery, Processing and Handling"
				+ " Fee",
				basePrice + deliveryPrice, totalPrice);

		driver.findElement(By.xpath("//*[@id=\"s_exterieur_x_FJ5\"]/span")).click();
		double equipPrice = parseCurrency(
				driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		printStepResult("Step 10 - Verifying that  Price for Equipment is Equal to Miami Blue price", 2580, equipPrice);

		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		printStepResult(
				"Step 11 - Verifying that total price is the sum of base price + Price for Equipment + Delivery,"
						+ "Processing and Handling Fee",
				basePrice + deliveryPrice + equipPrice, totalPrice);

		// --

		JavascriptExecutor executor = (JavascriptExecutor) driver;

		new WebDriverWait(driver, 10)
			.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#s_exterieur_x_MXRD")));

		// executor.executeScript("arguments[0].scrollIntoView();",
		// driver.findElement(By.cssSelector("#IRA_subHdl")));

		executor.executeScript("arguments[0].click();", driver.findElement(By.cssSelector("#s_exterieur_x_MXRD")));

		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		printStepResult(
				"Step 13 - Verifying that price for Equipment is the sum of Miami Blue price + 20 Carrera Sport"
				+ " Wheels",
				6330, equipPrice);

		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		printStepResult(
				"Step 14 - Verifying that total price is the sum of base price + Price for Equipment + Delivery," + 
				"Processing and Handling Fee",
				basePrice + deliveryPrice + equipPrice, totalPrice);

		// --

		executor.executeScript("arguments[0].click();",
				driver.findElement(By.cssSelector("#s_interieur_x_73_x_PP06_x_shorttext")));

		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		printStepResult(
				"Step 16 - Verifying that  Price for Equipment is the sum of Miami Blue price + 20\" Carrera Sport"
				+ "Wheels + Power Sport Seats (14-way) with Memory Package",
				8660, equipPrice);

		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		printStepResult(
				"Step 17 - Verifying that total price is the sum of base price + Price for Equipment + Delivery," + 
				"Processing and Handling Fee",
				basePrice + deliveryPrice + equipPrice, totalPrice);
		
		// #IIC_subHdl click
		// #vs_table_IIC_x_PEKH_x_c04_PEKH_x_shorttext click 
		// 10200
		
		executor.executeScript("arguments[0].click();",
				driver.findElement(By.cssSelector("#IIC_subHdl")));

		executor.executeScript("arguments[0].click();",
				driver.findElement(By.cssSelector("#vs_table_IIC_x_PEKH_x_c04_PEKH_x_shorttext")));
		
		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		printStepResult(
				"Step 20 - Verifying that Price for Equipment is the sum of Miami Blue price + 20\" Carrera Sport" + 
				"Wheels + Power Sport Seats (14-way) with Memory Package + Interior Trim in" + 
				"Carbon Fiber i.c.w. Standard Interior",
				10200, equipPrice);

		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		printStepResult(
				"Step 21 - Verifying that total price is the sum of base price + Price for Equipment + Delivery," + 
				"Processing and Handling Fee",
				basePrice + deliveryPrice + equipPrice, totalPrice);
		
		
		// #IMG_subHdl click
		// #vs_table_IMG_x_M250_x_c14_M250_x_shorttext click
		// #vs_table_IMG_x_M450_x_c94_M450_x_shorttext click
		// equip price 20820
		
		executor.executeScript("arguments[0].click();",
				driver.findElement(By.cssSelector("#IMG_subHdl")));

		executor.executeScript("arguments[0].click();",
				driver.findElement(By.cssSelector("#vs_table_IMG_x_M250_x_c14_M250_x_shorttext")));
		
		executor.executeScript("arguments[0].click();",
				driver.findElement(By.cssSelector("#vs_table_IMG_x_M450_x_c94_M450_x_shorttext")));
		
		equipPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[2]/div[2]")).getText());
		printStepResult(
				"Step 25 - Verifying that Price for Equipment is the sum of Miami Blue price + 20\" Carrera Sport" + 
				"Wheels + Power Sport Seats (14-way) with Memory Package + Interior Trim in" + 
				"Carbon Fiber i.c.w. Standard Interior + 7-speed Porsche Doppelkupplung (PDK) +" + 
				"Porsche Ceramic Composite Brakes (PCCB)",
				20820, equipPrice);

		totalPrice = parseCurrency(driver.findElement(By.xpath("//*[@id=\"s_price\"]/div[1]/div[4]/div[2]")).getText());
		printStepResult(
				"Step 26 - Verifying that total price is the sum of base price + Price for Equipment + Delivery," + 
				"Processing and Handling Fee",
				basePrice + deliveryPrice + equipPrice, totalPrice);
	}

}
