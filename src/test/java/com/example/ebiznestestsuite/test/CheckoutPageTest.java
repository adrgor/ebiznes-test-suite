package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.BasketPage;
import com.example.ebiznestestsuite.pom.CheckoutPage;
import com.example.ebiznestestsuite.pom.MainPageLoggedIn;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutPageTest {
    private WebDriver driver;
    private CheckoutPage checkoutPage;
    private String CHECKOUT_URL = "http://localhost:3000/checkout/";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:3000/checkout/");
        checkoutPage = new CheckoutPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testCurrentUrl() {
        assertEquals(checkoutPage.getUrl(), CHECKOUT_URL);
    }

    @Test
    void testElementsVisibility() {
        assertTrue(checkoutPage.emailAddressInput.isDisplayed());
        assertTrue(checkoutPage.repeatedEmailAddressInput.isDisplayed());
        assertTrue(checkoutPage.firstNameInput.isDisplayed());
        assertTrue(checkoutPage.lastNameInput.isDisplayed());
        assertTrue(checkoutPage.streetInput.isDisplayed());
        assertTrue(checkoutPage.buildingNumberInput.isDisplayed());
        assertTrue(checkoutPage.apartmentNumberInput.isDisplayed());
        assertTrue(checkoutPage.cityInput.isDisplayed());
        assertTrue(checkoutPage.postalCodeInput.isDisplayed());
        assertTrue(checkoutPage.countryInput.isDisplayed());
        assertTrue(checkoutPage.phoneNumberInput.isDisplayed());
        assertTrue(checkoutPage.summary.isDisplayed());
        assertTrue(checkoutPage.paymentButton.isDisplayed());
    }

    @Test
    void testFillFormAndGoNext() {
        fillForm();
        checkoutPage.paymentButton.click();
        assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div[2]")).getText(), "Go back");
    }

    @Test
    void testMissingEmailAddress() {
        fillForm();
        checkoutPage.emailAddressInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingRepeatEmailAddress() {
        fillForm();
        checkoutPage.repeatedEmailAddressInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingFirstName() {
        fillForm();
        checkoutPage.firstNameInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingLastName() {
        fillForm();
        checkoutPage.lastNameInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingStreet() {
        fillForm();
        checkoutPage.streetInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingBuildingNumber() {
        fillForm();
        checkoutPage.buildingNumberInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingCity() {
        fillForm();
        checkoutPage.cityInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingPostalCode() {
        fillForm();
        checkoutPage.postalCodeInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingCountry() {
        fillForm();
        checkoutPage.countryInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testMissingPhoneNumber() {
        fillForm();
        checkoutPage.phoneNumberInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Fill all the necessary fields!");
    }

    @Test
    void testNotSameEmails() throws InterruptedException {
        fillForm();
        checkoutPage.repeatedEmailAddressInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.repeatedEmailAddressInput.sendKeys("adrian.gorski@uj.edu.pl");
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Emails are not the same");
    }

    @Test
    void testEmailHasWrongFormat() {
        fillForm();
        checkoutPage.emailAddressInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.emailAddressInput.sendKeys("adrian.gorskiuj.edu.pl");
        checkoutPage.repeatedEmailAddressInput.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        checkoutPage.repeatedEmailAddressInput.sendKeys("adrian.gorskiuj.edu.pl");
        checkoutPage.paymentButton.click();
        assertEquals(driver.switchTo().alert().getText(), "Given email doens't have valid format");
    }

    @Test
    void testSummaryPriceForSingleItem() {
        fillBasket(1);
        assertEquals(checkoutPage.summary.getText(), "Total price: $899.00");
    }

    @Test
    void testSummaryPriceForMultipleUniqueItems() {
        fillBasket(2);
        assertEquals(checkoutPage.summary.getText(), "Total price: $1998.99");
    }

    @Test
    void testSummaryPriceForMultipleNotUniqueItems() {
        fillBasketWithSameElements(2);
        assertEquals(checkoutPage.summary.getText(), "Total price: $1798.00");
    }

    @Test
    void testSummaryForSingleFloatPrice() {
        fillBasket(1,1);
        assertEquals(checkoutPage.summary.getText(), "Total price: $1099.99");
    }

    @Test
    void testSummaryPriceForMultipleFloatPrice() {
        fillBasketWithSameElements(1,5);
        assertEquals(checkoutPage.summary.getText(), "Total price: $5499.95");
    }

    @Test
    void testSummaryPriceForNotUniquePrices() {
        fillBasketWithSameElements(0,5);
        assertEquals(checkoutPage.summary.getText(), "Total price: $4495.00");
    }

    @Test
    void testRememberSummaryState() {
        fillBasket(1);
        driver.findElement(By.xpath("//*[@id=\"header\"]/a[1]/div")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).click();
        driver.findElement(By.className("checkout")).click();
        assertEquals(checkoutPage.summary.getText(), "Total price: $899.00");
    }

    private void fillBasket(int numOfElements) {
        fillBasket(0, numOfElements);
    }

    private void fillBasket(int startIndex, int numOfElements) {
        driver.get("http://localhost:3000/");
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        localStorage.setItem("JWT_TOKEN", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIn0._xmOH2Neznww4qbbbqDPMge6y_OnptJqRc4w5vXUxgJUAkkyvzpHZyBkeoD35YxCMYABwo_Cy7YN91CJGOOsRQ");
        driver.get("http://localhost:3000/");

        for (int i = startIndex; i < startIndex + numOfElements; i++) {
            new MainPageLoggedIn(driver).items.get(i).findElement(By.xpath(".//div[3]")).click();
        }

        driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).click();
        driver.findElement(By.className("checkout")).click();
    }


    private void fillBasketWithSameElements(int numOfElements) {
        fillBasketWithSameElements(0, numOfElements);
    }

    private void fillBasketWithSameElements(int startIndex, int numOfElements) {
        driver.get("http://localhost:3000/");
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        localStorage.setItem("JWT_TOKEN", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIn0._xmOH2Neznww4qbbbqDPMge6y_OnptJqRc4w5vXUxgJUAkkyvzpHZyBkeoD35YxCMYABwo_Cy7YN91CJGOOsRQ");
        driver.get("http://localhost:3000/");
        for (int i = 0; i < numOfElements; i++) {
            new MainPageLoggedIn(driver).items.get(startIndex).findElement(By.xpath(".//div[3]")).click();
        }
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).click();
        driver.findElement(By.className("checkout")).click();
    }

    private void fillForm() {
        checkoutPage.emailAddressInput.sendKeys("adrian.gorski@student.uj.edu.pl");
        checkoutPage.repeatedEmailAddressInput.sendKeys("adrian.gorski@student.uj.edu.pl");
        checkoutPage.firstNameInput.sendKeys("Adrian");
        checkoutPage.lastNameInput.sendKeys("Górski");
        checkoutPage.streetInput.sendKeys("Warszawksa");
        checkoutPage.buildingNumberInput.sendKeys("12");
        checkoutPage.apartmentNumberInput.sendKeys("12");
        checkoutPage.cityInput.sendKeys("Kraków");
        checkoutPage.postalCodeInput.sendKeys("30-340");
        checkoutPage.countryInput.sendKeys("Polksa");
        checkoutPage.phoneNumberInput.sendKeys("123123123");
    }
}
