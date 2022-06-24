package com.example.ebiznestestsuite.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage {

    private WebDriver driver;

    @FindBy(id = "footer")
    public WebElement footer;

    @FindBy(id = "header")
    public WebElement header;

    @FindBy(xpath = "//*[@id=\"header\"]/a[3]")
    public WebElement loginButton;

    @FindBy(xpath = "//*[@id=\"header\"]/a[1]")
    public WebElement homeButton;

    @FindBy(xpath = "//*[@id=\"header\"]/a[2]")
    public WebElement basketButton;

    @FindBy(className = "item-list")
    public WebElement itemList;

    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
