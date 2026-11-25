package com.demoproject.base;

import com.demoproject.actiondriver.ActionDriver;
import com.demoproject.utilities.ExtentManager;
import com.demoproject.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseClass {

    protected static Properties prop;
    //protected static WebDriver driver;
    //private static ActionDriver actionDriver;
    private static ThreadLocal<WebDriver> driver =new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver =new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void loadConfig() throws IOException {
        //Load the config file
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        //start the extent report
        ExtentManager.getReporter();
    }

    private void launchBrowser(){
        //Initialize webdriver based on browser defined in config.properties
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            //driver = new ChromeDriver();
            driver.set(new ChromeDriver()); //new changes as per thread
            ExtentManager.registerDriver(getDriver());
            logger.info("ChromeDriver instance is created");
        } else if (browser.equalsIgnoreCase("firefox")) {
            //driver = new FirefoxDriver();
            driver.set(new FirefoxDriver());
            ExtentManager.registerDriver(getDriver());
            logger.info("FirefoxDriver instance is created");
        } else if (browser.equalsIgnoreCase("edge")) {
            //driver = new EdgeDriver();
            driver.set(new EdgeDriver());
            ExtentManager.registerDriver(getDriver());
            logger.info("EdgeDriver instance is created");
        } else if (browser.equalsIgnoreCase("internetexplorer")) {
            //driver = new InternetExplorerDriver();
            driver.set(new InternetExplorerDriver());
            ExtentManager.registerDriver(getDriver());
            logger.info("InternetExplorerDriver instance is created");
        } else {
            throw new IllegalArgumentException("browser not supported"+browser);
        }
        logger.info("browser is launched");
    }

    private void configureBrowser(){
        //Implicit Wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        getDriver().manage().window().maximize();
        logger.info("browser is maximized");

        try {
            getDriver().get(prop.getProperty("url"));
        }catch(Exception e){
            System.out.println("Failed to navigate to URL :"+e.getMessage());
        }
    }

    @BeforeMethod
    public void setUp() throws IOException {
        try {
        System.out.println("setting up webdriver for: "+this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        Thread.sleep(1000);
        logger.trace("this is trace message");
        logger.error("this is error message");
        logger.debug("this is debug message");
        logger.fatal("this is fatal message");
        logger.warn("this is warn message");

        /*//Initialize the actionDriver only once --Singleton pattern
            if(actionDriver == null){
                actionDriver = new ActionDriver(driver);
                logger.info("ActionDriver instance is created"+ Thread.currentThread().getId());
                //System.out.println("ActionDriver instance is created");
            }*/

            //Initialize ActionDriver for current thread
            actionDriver.set( new ActionDriver(getDriver()));
            logger.info("ActionDriver initialized for thread "+ Thread.currentThread().getId());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterMethod
    public void tearDown(){
        if(driver!=null){
            try {
                getDriver().quit();
            }catch(Exception e){
                System.out.println("Failed to quit the driver :"+e.getMessage());
            }
        }
        //driver =null;
        //actionDriver=null;
        driver.remove();
        actionDriver.remove();
        logger.info("WebDriver instance is closed");
        //System.out.println("WebDriver instance is closed");
        ExtentManager.endTest();
    }

    //Prop getter method
    public static Properties getProp(){
        return prop;
    }
    /*
    //driver getter method
    public WebDriver getDriver(){
        return driver;
    }

    //driver setter method
    public void setDriver(WebDriver driver){
        this.driver=driver;
    }*/

    //getter method for WebDriver
    public static WebDriver getDriver(){
        if(driver.get()==null){
            System.out.println("WebDriver is not Initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();
    }

    //getter method for ActionDriver
    public static ActionDriver getActionDriver(){
        if(actionDriver.get()==null){
            System.out.println("ActionDriver is not Initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

}
