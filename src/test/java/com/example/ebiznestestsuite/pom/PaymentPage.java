package com.example.ebiznestestsuite.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PaymentPage {
    WebDriver driver;

    @FindBy(id = "payment-form")
    public WebElement paymentForm;

    @FindBy(id = "Field-numberInput")
    public WebElement cardNumberInput;

    @FindBy(id = "Field-expiryInput")
    public WebElement expiryInput;

    @FindBy(id = "Field-cvcInput")
    public WebElement cvcInput;

    @FindBy(id = "Field-countryInput")
    public WebElement countryInput;

    @FindBy(id = "submit")
    public WebElement payBtn;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[2]/div[1]")
    public WebElement summary;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[2]/div[2]")
    public WebElement backBtn;

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
