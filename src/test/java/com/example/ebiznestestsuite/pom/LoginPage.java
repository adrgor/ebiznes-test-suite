package com.example.ebiznestestsuite.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver driver;

    @FindBy(className = "login-form")
    public WebElement loginForm;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/a[1]")
    public WebElement googleLoginButton;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/a[2]")
    public WebElement githubLoginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
