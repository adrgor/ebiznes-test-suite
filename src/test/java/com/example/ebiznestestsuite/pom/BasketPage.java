package com.example.ebiznestestsuite.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BasketPage {
    WebDriver driver;

    @FindBy(className = "basket")
    public WebElement basket;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/p")
    public WebElement basketParagraph;

    @FindBy(className = "summary")
    public WebElement summary;

    public BasketPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
