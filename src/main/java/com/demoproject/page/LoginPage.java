package com.demoproject.page;

import com.demoproject.actiondriver.ActionDriver;
import com.demoproject.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionDriver actionDriver;

    //Define locators using By class
    private By username= By.name("username") ;
    private By password= By.cssSelector("input[type='password']");
    private By loginBtn= By.xpath("//button[text()=' Login ']") ;
    private By loginErrormsg= By.xpath("//p[text()='Invalid credentials']") ;

//    //Initialize ActionDriver Object by passing webdriver Instance
//    public LoginPage(WebDriver driver){
//        this.actionDriver= new ActionDriver(driver);
//    }
    public LoginPage(WebDriver driver){
        this.actionDriver= BaseClass.getActionDriver();
    }


    //method to perform login
    public void login(String uname, String pass){
        actionDriver.enterText(username,uname);
        actionDriver.enterText(password,pass);
        actionDriver.click(loginBtn);
    }

    //method to check error message
    public boolean isLoginErrorMsgDisplayed(){
        return actionDriver.isDisplayed(loginErrormsg);
    }

}
