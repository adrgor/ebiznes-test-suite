package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.CheckoutPage;
import com.example.ebiznestestsuite.pom.MainPageLoggedIn;
import com.example.ebiznestestsuite.pom.PaymentPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentPageTest {

    private WebDriver driver;
    private PaymentPage paymentPage;
    private CheckoutPage checkoutPage;

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
        fillForm();
        checkoutPage.paymentButton.click();
        paymentPage = new PaymentPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testVisibility() {
        fillBasket(1);
        assertTrue(paymentPage.paymentForm.isDisplayed());
        assertTrue(paymentPage.backBtn.isDisplayed());
        assertTrue(paymentPage.payBtn.isDisplayed());
        assertTrue(paymentPage.summary.isDisplayed());
    }

    @Test
    void testIFrameVisibility() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        assertTrue(driver.findElement(By.id("Field-numberInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("Field-expiryInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("Field-cvcInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("Field-countryInput")).isDisplayed());
    }

    @Test
    void testPayWithCorrectDetails() throws InterruptedException {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4242424242424242");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0525");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        Thread.sleep(10000);
        assertTrue(driver.getCurrentUrl().matches("http:\\/\\/localhost:3000\\/.*"));
    }

    @Test
    void testIncompleteCardNumber() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("424242424242424");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0525");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        assertEquals(driver.findElement(By.id("Field-numberError")).getText(), "Numer karty jest niepełny.");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Numer karty jest niepełny.");
    }

    @Test
    void testIncorrectCardNumber() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4242424242424241");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0525");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        assertEquals(driver.findElement(By.id("Field-numberError")).getText(), "Numer karty jest nieprawidłowy.");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Numer karty jest nieprawidłowy.");
    }

    @Test
    void testPassedExpiryDate() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4242424242424242");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0520");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        assertEquals(driver.findElement(By.id("Field-expiryError")).getText(), "Rok ważności karty już minął.");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Rok ważności karty już minął.");
    }

    @Test
    void testIncorrectExpiryDate() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4242424242424242");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0599");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        assertEquals(driver.findElement(By.id("Field-expiryError")).getText(), "Rok ważności karty jest nieprawidłowy.");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Rok ważności karty jest nieprawidłowy.");
    }

    @Test
    void testIncompleteExpiryDate() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-expiryInput")).sendKeys("011");
        driver.findElement(By.id("Field-numberInput")).sendKeys("4242424242424242");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        assertEquals(driver.findElement(By.id("Field-expiryError")).getText(), "Data ważności karty jest niepełna.");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Data ważności karty jest niepełna.");
    }

    @Test
    void testIncompleteCVC() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4242424242424242");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("11");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        assertEquals(driver.findElement(By.id("Field-cvcError")).getText(), "Kod zabezpieczający karty jest niepełny.");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Kod zabezpieczający karty jest niepełny.");
    }

    @Test
    void testEmptyBasket() {
        driver.get("http://localhost:3000/checkout/");
        fillForm();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div[2]")).click();
        assertTrue(driver.findElements(By.id("payment-form")).size() == 0);
    }

    @Test
    void testGenericDeclinedCard() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4000000000000002");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Karta została odrzucona.");
    }

    @Test
    void testInsufficientFundsDeclinedCard() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4000000000009995");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Za mało środków na karcie.");
    }

    @Test
    void testLostCardDecline() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4000000000009987");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Karta została odrzucona.");
    }

    @Test
    void testStolenCardDecline() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4000000000009979");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Karta została odrzucona.");
    }

    @Test
    void testExpiredCardDeclined() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4000000000000069");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Karta straciła ważność.");
    }

    @Test
    void testIncorrectCVCDecline() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4000000000000127");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Kod zabezpieczający karty jest niepoprawny.");
    }

    @Test
    void testProcessingErrorDecline() {
        fillBasket(1);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"payment-element\"]/div/iframe"));
        driver.switchTo().frame(iFrame);
        driver.findElement(By.id("Field-numberInput")).sendKeys("4000000000000119");
        driver.findElement(By.id("Field-cvcInput")).sendKeys("111");
        driver.findElement(By.id("Field-expiryInput")).sendKeys("0125");
        driver.findElement(By.id("Field-countryInput")).sendKeys("Poland");
        driver.switchTo().defaultContent();
        paymentPage.payBtn.click();
        assertEquals(driver.findElement(By.id("payment-message")).getText(), "Podczas przetwarzania danych karty wystąpił błąd. Spróbuj ponownie za jakiś czas.");
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
        fillForm();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div[2]")).click();
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
        fillForm();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div[2]")).click();
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
