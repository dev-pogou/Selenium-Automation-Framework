package com.demoproject.test;

import com.demoproject.base.BaseClass;
import com.demoproject.page.HomePage;
import com.demoproject.page.LoginPage;
import com.demoproject.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPage(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyOrangeHRMLogo(){
        ExtentManager.startTest("CheckLogo");
        ExtentManager.logStep("Navigating to Home page and checking for logo");
        loginPage.login("Admin","admin123");
        ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"entered details - checking for logo","logo");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(),"******Logo is not visible");
        ExtentManager.logStep("Verified successfully");
    }
}
