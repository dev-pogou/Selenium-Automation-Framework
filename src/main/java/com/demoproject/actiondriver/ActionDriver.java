package com.demoproject.actiondriver;

import com.demoproject.base.BaseClass;
import com.demoproject.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver {
    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;

    public ActionDriver(WebDriver driver){
        this.driver = driver;
        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("WebDriver instance is created");
    }

    //method to click an element
    public void click(By by){
        try{
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("clicked an element");
            logger.info("Element is clicked");
        } catch (Exception e) {
            System.out.println("Unable to click the element :"+e.getMessage());
            ExtentManager.logFailure(BaseClass.getDriver(),"Unable to click element","");
            logger.error("Unable to click the element");
        }
    }

    //method to enter text
    public void enterText(By by, String value){
        try{
            waitForElementToBeVisible(by);
            WebElement element =driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Element is entered");
        } catch (Exception e) {
            logger.error("Unable to enter the element :"+e.getMessage());
        }
    }

    //method to compare text
    public void compareText(By by, String expectedText){
        try{
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Compare Text", "Text verified successfully! "+actualText+" equals "+expectedText);
            if(expectedText.equals(actualText)){
                logger.info("Text are Matching: " +actualText+ " equals " +expectedText);
            }else{
                ExtentManager.logFailure(BaseClass.getDriver(),"Compare Text", "Text comparison Failed! "+actualText+" not equals "+expectedText);
                logger.error("Text are not Matching: " +actualText+ " Not equals " +expectedText);
            }
        } catch (Exception e) {
            System.out.println("Unable to compare the element :"+e.getMessage());
        }
    }

    //method to get text
    public String getText(By by){
        try{
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Unable to get the element :"+e.getMessage());
            return "";
        }
    }

    //check if element is displayed
    public Boolean isDisplayed(By by){
        try{
            waitForElementToBeVisible(by);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            logger.error("element is not visible:"+e.getMessage());
            return false;
        }
    }

    //Scroll to an element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0],scrollIntoView(true)", driver.findElement(by));
        } catch (Exception e) {
            logger.error("Unable to locate element :"+e.getMessage());
        }
    }

    //wait for element to be clickable
    private void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            System.out.println("element is not clickable :"+e.getMessage());
        }
    }

    //wait for element to be visisble
    private void waitForElementToBeVisible(By by){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("element is not visisble :"+e.getMessage());
        }
    }

}
