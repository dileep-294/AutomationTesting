package com.test1;

import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class LocaleTimeTest {

    WebDriver webDriver;

    @BeforeClass
    public void loadBefore(){
        WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.get("https://savvytime.com");
    }
    //@Parameters({"firstTimeZone"})
    @Test(description = "Getting the Time of a particular city")

    public void gettingLocalTime(){
        String firstTimeZone="Hyderabad";
        webDriver.findElement(By.xpath("//a[@href='/local']")).click();
        WebElement webElement = webDriver.findElement(By.xpath("//h5[@class='logo-text']"));
        Assert.assertTrue(webElement.getText().contains("Savvy Time"),
                "Expected text contains Time Zone Converter-Savvy Time but found"+webElement.getText());

        webDriver.findElement(By.xpath("//input[@placeholder='Search cities or countries']")).sendKeys(firstTimeZone);

        webDriver.findElements(By.xpath("//div[@id='home-quick-search-result']//a")).get(0).click();
        Assert.assertTrue(webDriver.findElement(By.xpath("//h1[@class='title']")).getText().contains("Hyderabad Time"));

        String currentHours=webDriver.findElement(By.xpath("//span[@class='current-hours']")).getText();
        String currentMinutes=webDriver.findElement(By.xpath("//span[@class='current-minutes']")).getText();
        System.out.println(currentHours+":"+currentMinutes);

    }

    @AfterClass
    public void exit(){
        webDriver.quit();
    }
}
