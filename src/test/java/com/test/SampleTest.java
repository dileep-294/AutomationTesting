package com.test;

import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SampleTest {

    WebDriver webDriver;

    @BeforeClass
    public void loadBefore(){
        WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //webDriver.get("https://savvytime.com/converter");
    }


    @BeforeMethod
    @Parameters({"firstTimeZone","secondTimeZone"})
    public void loadBeforeMethod(String firstTimeZone,String secondTimeZone){
        webDriver.get("https://savvytime.com/converter");
        webDriver.findElement(By.xpath("//input[@placeholder='Add Time Zone, City or Town']")).sendKeys(firstTimeZone);
        webDriver.findElements(By.xpath("//div[@id='converter-quick-search-result']//a")).get(0).click();
        webDriver.findElement(By.xpath("//input[@placeholder='Add Time Zone, City or Town']")).sendKeys(secondTimeZone);
        webDriver.findElements(By.xpath("//div[@id='converter-quick-search-result']//a")).get(0).click();
    }

    @Test(description = "Verifying whether the page is loaded correctly or not")
    public void pageTitle(){
        WebElement webElement = webDriver.findElement(By.xpath("//h5[@class='logo-text']"));
        Assert.assertTrue(webElement.getText().contains("Savvy Time"),
                "Expected text contains Time Zone Converter-Savvy Time but found"+webElement.getText());
    }

    @Test(description = "Adding First zone and verifying it")
    @Parameters({"firstTimeZone"})
    public void addFirstCity(String firstTimeZone)
    {
        WebElement element = webDriver.findElement(By.xpath("//h1[@class='title']"));
        Assert.assertTrue(element.getText().contains(firstTimeZone),
                "Expected text contains Hyderabad but found"+element.getText());
    }

    @Parameters({"secondTimeZone"})
    @Test(description = "Adding second city and verifying")
    public void addSecondCity(String secondTimeZone){
        WebElement element = webDriver.findElement(By.xpath("//h1[@class='title']"));
        Assert.assertTrue(element.getText().contains(secondTimeZone),
                "Expected text contains Hyderabad but found"+element.getText());
    }

    @Test(description = "fetching times of two different zones and calculating the TimeDifference")
    public void timeDifference() throws ParseException {
        List<WebElement> timeZoneElements= webDriver.findElements
                (By.xpath("//input[@class='time ampm format12 form-control ui-timepicker-input']"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        Date date1=simpleDateFormat.parse(timeZoneElements.get(0).getAttribute("value"));
        Date date2 = simpleDateFormat.parse(timeZoneElements.get(1).getAttribute("value"));
        int timeDifference = (int) (Math.abs(date1.getTime()-date2.getTime())/60000);
        Assert.assertEquals(timeDifference,330,"Expected result 330 but found "+timeDifference);
    }


    @Test(description = "Verifying Swap Button")
    public void swapButton(){
        List<WebElement> webElementsBeforeSwap = webDriver.findElements(By.xpath("//div[@class='table-time row']"));
        List<String> stringListBeforeSwap = new ArrayList<String>(webElementsBeforeSwap.size());
        for (int i = 0; i < webElementsBeforeSwap.size(); i++) {
            stringListBeforeSwap.add(webElementsBeforeSwap.get(i).getText());
        }
        webDriver.findElement(By.className("icon-exchange")).click();
        List<WebElement> webElementsAfterSwap = webDriver.findElements(By.xpath("//div[@class='table-time row']"));
        List<String> stringListAfterSwap = new ArrayList<String>(webElementsBeforeSwap.size());
        for (int i = 0; i < webElementsBeforeSwap.size(); i++) {
            stringListAfterSwap.add(webElementsAfterSwap.get(i).getText());
        }
        Collections.reverse(stringListAfterSwap);
        Assert.assertEquals(stringListBeforeSwap, stringListAfterSwap);
    }

    @Test(description = "Modifying the date of cities")
    public void modifyDate(){
        webDriver.findElement(By.id("time-search")).sendKeys("Hyderabad");
        List<WebElement> elements = webDriver.findElements(By.xpath("//div[@id='converter-quick-search-result']//a"));
        elements.get(0).click();
        WebElement element = webDriver.findElement(By.xpath("//h1[@class='title']"));
        Assert.assertTrue(element.getText().contains("Hyderabad"),
                "Expected text contains Hyderabad but found"+element.getText());
        webDriver.findElement(By.className("icon-table")).click();
        webDriver.findElement(By.xpath("//div[@class='datepicker-days']//table[@class=' table-condensed']//tr[4]//td[2]")).click();
        WebElement webElement = (webDriver.findElement(By.xpath("//div[@class='tz-date']")));
        Assert.assertTrue(webElement.getText().contains("20"));
    }

    @Test(description = "Deleting the Citie")
    public void deleteTimeZone(){
        List<WebElement> webElements =  webDriver.findElements(By.xpath("//div[@class='table-time row']"));
        int noOfWebElementsBefore = webElements.size();
        webElements.get(0).click();
        webDriver.findElement(By.xpath("//a[@class='delete-btn btn']")).click();
        List<WebElement> webDriverElements =  webDriver.findElements(By.xpath("//div[@class='table-time row']"));
        int noOfWebElementsAfter = webDriverElements.size();
        Assert.assertEquals(noOfWebElementsBefore,noOfWebElementsAfter+1);
    }

    @Test(description = "Checking permanantLinkhttps://github.com/dileep-294/AutomationTesting.git")
    public void permanantLink(){
        webDriver.findElement(By.xpath("//a[@id='permanent-link']")).click();
        webDriver.findElement(By.id("include-time")).click();
        webDriver.findElement(By.id("include-date")).click();
        String url = webDriver.findElement(By.xpath("//input[@id='share-url']")).getAttribute("value");
        System.out.println(url);
    }

    @AfterClass
    public void exit()
    {
        webDriver.quit();
    }
}
