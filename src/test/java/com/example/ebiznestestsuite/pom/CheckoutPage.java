package com.example.ebiznestestsuite.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckoutPage {
    WebDriver driver;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[1]")
    public WebElement emailAddressInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[2]")
    public WebElement repeatedEmailAddressInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[3]")
    public WebElement firstNameInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[4]")
    public WebElement lastNameInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[5]")
    public WebElement streetInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[6]")
    public WebElement buildingNumberInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[7]")
    public WebElement apartmentNumberInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[8]")
    public WebElement cityInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[9]")
    public WebElement postalCodeInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[10]")
    public WebElement countryInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/form/input[11]")
    public WebElement phoneNumberInput;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[2]/div[1]")
    public WebElement summary;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div[2]/div[2]/div[2]")
    public WebElement paymentButton;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }
}
