package com.example.ebiznestestsuite.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddProductPage {
    WebDriver driver;

    @FindBy(id = "name-input")
    public WebElement nameInput;

    @FindBy(id = "category-input")
    public WebElement categoryInput;

    @FindBy(id = "price-input")
    public WebElement priceInput;

    @FindBy(id = "submit-btn")
    public WebElement submitBtn;

    public AddProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
