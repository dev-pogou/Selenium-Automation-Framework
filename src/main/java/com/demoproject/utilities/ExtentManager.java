package com.demoproject.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap= new HashMap<>();

    //Initialize Extent Report
    public synchronized static ExtentReports getReporter(){
        if(extentReports==null){
            String reportPath= System.getProperty("user.dir")+"/src/test/resources/ExtentReport/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("Demo Project Report");
            spark.config().setTheme(Theme.DARK);

            extentReports= new ExtentReports();
            extentReports.attachReporter(spark);
            //Adding system information
            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
        }
        return extentReports;
    }

    //start the test
    public synchronized static ExtentTest startTest(String testName){
        ExtentTest extentTest = getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    //end the test
    public synchronized static void endTest(){
        getReporter().flush();
    }

    //get current threads test
    public static ExtentTest getTest(){
        return test.get();
    }

    //method to get the name of current test
    public static String getTestName(){
        ExtentTest currentTest =getTest();
        if(currentTest!=null){
            return currentTest.getModel().getName();
        }else{
            return "No test is currently active for this thread";
        }
    }

    //log a step
    public static void logStep(String logMessage){
        getTest().info(logMessage);
    }

    //log a step validation with screenshot
    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage){
        getTest().pass(logMessage);
        //screenshot method
        attachScreenshot(driver,screenshotMessage);
    }

    //log a failure
    public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage){
        getTest().fail(logMessage);
        //screenshot method
        attachScreenshot(driver,screenshotMessage);
    }

    //log a skip
    public static void logSkip(WebDriver driver, String logMessage, String screenshotMessage){
        getTest().skip(logMessage);
    }

    //take a screenshot with date and time in the file
    public synchronized static String takeScreenshot(WebDriver driver, String screenshotName){
        TakesScreenshot ts= (TakesScreenshot)driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        //format date and time for file name
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        //saving the screenshot to file
        String destPath=System.getProperty("user.dir")+"/src/test/resources/Screenshots/"+screenshotName+"_"+timestamp+".png";
        File finalPath= new File(destPath);

        try {
            finalPath.getParentFile().mkdirs();
            System.out.println("Source: " + src.getAbsolutePath());
            System.out.println("Destination: " + finalPath.getAbsolutePath());
            System.out.println("Screenshot name: " + screenshotName);
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //convert screenshot to base64 for embedding the report
        String base64Format=convertToBase64(finalPath);
        return base64Format;
    }

    //Convert screenshot to base64 format
    public static String convertToBase64(File screenshotFile){
        String base64Format="";
        //Read the file content into byte array
        try {
            byte[] filecontent = FileUtils.readFileToByteArray(screenshotFile);
            base64Format = Base64.getEncoder().encodeToString(filecontent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return base64Format;
    }

    //attach screenshot to report using base64
    public static void attachScreenshot(WebDriver driver, String message){
        try {
            String screenshotBase64 = takeScreenshot(driver, getTestName());
            getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
        } catch (Exception e){
            getTest().fail("failed to attach screenshot:"+message);
            e.printStackTrace();
        }
    }

    //Register WebDriver for current thread
    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().getId(), driver);
    }
}
