package com.demoproject.test;

import com.demoproject.base.BaseClass;
import com.demoproject.page.HomePage;
import com.demoproject.page.LoginPage;
import com.demoproject.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPage(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void test1(){
        String title = getDriver().getTitle();
        System.out.println(title);
        assert title.equals("OrangeHRM") : "Title is not Matching" ;
    }

    @Test
    public void verifyValidLoginTest() throws InterruptedException {
        ExtentManager.startTest("Valid Login Test");
        ExtentManager.logStep("Navigating to login page entering username and password");
        loginPage.login("Admin","admin123");
        Thread.sleep(1000);
        Assert.assertTrue(homePage.isAdminTabVisible(),"******Admin tab is not visible");
        homePage.clickUserLogout();
        ExtentManager.logStep("Logged out successfully");
    }

    @Test
    public void checkInvalidLoginTest(){
        ExtentManager.startTest("InValid Login Test");
        ExtentManager.logStep("Navigating to login page entering invalid username and password");
        loginPage.login("Admin","admin1243");
        System.out.println("*******test*****");
        loginPage.isLoginErrorMsgDisplayed();
        ExtentManager.logStep("Logged out successfully");
    }

}
