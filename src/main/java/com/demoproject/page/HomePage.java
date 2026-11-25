package com.demoproject.page;

import com.demoproject.actiondriver.ActionDriver;
import com.demoproject.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private ActionDriver actionDriver;

    //Define locators using By class
    private By adminTab= By.xpath("//span[text()='Admin']") ;
    private By userIdButton= By.className("oxd-userdropdown-tab");
    private By logoutButton= By.xpath("//a[text()='Logout']") ;
    private By orangeHRMLogo= By.xpath("//div[@class='oxd-brand-banner']//img") ;

    //Initialize ActionDriver Object by passing webdriver Instance
//    public HomePage(WebDriver driver){
//        this.actionDriver= new ActionDriver(driver);
//    }

    public HomePage(WebDriver driver){
        this.actionDriver= BaseClass.getActionDriver();
    }

    //method to verify if admin tab is visible
    public boolean isAdminTabVisible(){
        return actionDriver.isDisplayed(adminTab);
    }

    public boolean verifyOrangeHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    //method to perform logout
    public void clickUserLogout(){
        actionDriver.click(userIdButton);
        actionDriver.click(logoutButton);
    }

}
