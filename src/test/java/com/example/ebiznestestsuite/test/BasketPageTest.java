package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.BasketPage;
import com.example.ebiznestestsuite.pom.MainPageLoggedIn;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasketPageTest {
    private WebDriver driver;
    private BasketPage basketPage;
    private String BASKET_URL = "http://localhost:3000/basket/";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:3000/basket/");
        basketPage = new BasketPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testCurrentUrl() {
        assertEquals(basketPage.getUrl(), BASKET_URL);
    }

    @Test
    void testMainPageVisibility() {
        assertTrue(basketPage.basket.isDisplayed());
        assertTrue(basketPage.basketParagraph.isDisplayed());
        assertTrue(basketPage.summary.isDisplayed());
    }

    @Test
    void testSingleElementInBasket() {
        fillBasket(1);
        assertEquals(driver.findElements(By.className("basket-item")).size(), 1);
        assertTrue(driver.findElement(By.className("checkout")).isDisplayed());
    }

    @Test
    void testRemoveSingleElementFromBasket() {
        fillBasket(1);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]")).click();
        assertEquals(driver.findElements(By.className("checkout")).size(), 0);
    }

    @Test
    void testAddMultipleUniqueElementsToBasket() {
        fillBasket(10);
        assertEquals(driver.findElements(By.className("basket-item")).size(), 10);
    }

    @Test
    void testAddSameElementToBasket() {
        fillBasketWithSameElements(2);
        List<WebElement> basketItems = driver.findElements(By.className("basket-item"));
        assertEquals(basketItems.get(0).findElement(By.xpath("//div[1]")).getText(), basketItems.get(1).findElement(By.xpath("//div[1]")).getText());
    }

    @Test
    void removeUniqueElementsFromBasket() {
        fillBasket(10);
        for (int i = 10; i >= 1; i--) {
            String xpath = "//*[@id=\"root\"]/div/div[2]/div[" + i + "]/div[2]";
            driver.findElement(By.xpath(xpath)).click();
        }
        assertEquals(driver.findElements(By.className("basket-item")).size(), 0);
    }

    @Test
    void removeSingleUniqueElementFromBasket() {
        fillBasket(10);
        String xpath = "//*[@id=\"root\"]/div/div[2]/div[10]/div[2]";
        driver.findElement(By.xpath(xpath)).click();
        assertEquals(driver.findElements(By.className("basket-item")).size(), 9);
    }

    @Test
    void removeSingleNotUniqueElementFromBasket() {
        fillBasketWithSameElements(10);
        String xpath = "//*[@id=\"root\"]/div/div[2]/div[10]/div[2]";
        driver.findElement(By.xpath(xpath)).click();
        assertEquals(driver.findElements(By.className("basket-item")).size(), 9);
    }

    @Test
    void removeNotUniqueElementsFromBasket() {
        fillBasketWithSameElements(10);
        for (int i = 10; i >= 1; i--) {
            String xpath = "//*[@id=\"root\"]/div/div[2]/div[" + i + "]/div[2]";
            driver.findElement(By.xpath(xpath)).click();
        }
        assertEquals(driver.findElements(By.className("basket-item")).size(), 0);
    }

    @Test
    void testSummaryPriceForSingleItem() {
        fillBasket(1);
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $899.00");
    }

    @Test
    void testSummaryPriceForMultipleUniqueItems() {
        fillBasket(2);
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $1998.99");
    }

    @Test
    void testSummaryPriceForMultipleNotUniqueItems() {
        fillBasketWithSameElements(2);
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $1798.00");
    }

    @Test
    void testSummaryForSingleFloatPrice() {
        fillBasket(1,1);
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $1099.99");
    }

    @Test
    void testSummaryPriceForMultipleFloatPrice() {
        fillBasketWithSameElements(1,5);
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $5499.95");
    }

    @Test
    void testSummaryPriceForNotUniquePrices() {
        fillBasketWithSameElements(0,5);
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $4495.00");
    }

    @Test
    void testSummaryPriceAfterSingleItemRemoval() {
        fillBasket(1);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]")).click();
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $0.00");
    }

    @Test
    void testSummaryPriceAfterRemovingSingleItemFromNotUniqueSet() {
        fillBasketWithSameElements(5);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]")).click();
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $3596.00");
    }

    @Test
    void testSummaryPriceAfterSingleItemFloatRemoval() {
        fillBasket(1, 1);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]")).click();
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $0.00");
    }

    @Test
    void testSummaryPriceAfterRemovingSingleFloatItemFromNotUniqueSet() {
        fillBasketWithSameElements(1,5);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]")).click();
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $4399.96");
    }

    @Test
    void testSummaryPriceAfterRemovingAllFloatItems() {
        fillBasketWithSameElements(1,5);
        for (int i = 0; i < 5; i++) {
            driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]")).click();
        }
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $0.00");
    }

    @Test
    void testRememberBasketState() {
        fillBasket(1);
        driver.findElement(By.xpath("//*[@id=\"header\"]/a[1]/div")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).click();
        assertTrue(driver.findElement(By.className("basket-item")).isDisplayed());
    }

    @Test
    void testRememberSummaryState() {
        fillBasket(1);
        driver.findElement(By.xpath("//*[@id=\"header\"]/a[1]/div")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).click();
        assertEquals(driver.findElement(By.className("summary")).getText(), "Summary: $899.00");
    }

    @Test
    void testLogoutFromBasket() {
        fillBasket(1);
        driver.findElement(By.xpath("//*[@id=\"header\"]/a[3]")).click();
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/");
        assertEquals(driver.findElements(By.className("item")).size(), 0);
    }

    @Test
    void testBasketStateAfterLogout() {
        fillBasket(1);
        driver.findElement(By.xpath("//*[@id=\"header\"]/a[3]")).click();
        driver.findElement(By.xpath("//*[@id=\"header\"]/a[2]")).click();
        assertEquals(driver.findElements(By.className("basket-item")).size(), 0);
    }

    @Test
    void testGoToCheckout() {
        fillBasket(1);
        driver.findElement(By.className("checkout")).click();
        assertEquals("http://localhost:3000/checkout", driver.getCurrentUrl());
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
    }
}
