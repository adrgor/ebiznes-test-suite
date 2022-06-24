package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.*;
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

public class AddProductPageTest {

    private WebDriver driver;
    private AdminPage adminPage;
    private AddProductPage addProductPage;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:3000/");
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        localStorage.setItem("JWT_TOKEN", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIn0._xmOH2Neznww4qbbbqDPMge6y_OnptJqRc4w5vXUxgJUAkkyvzpHZyBkeoD35YxCMYABwo_Cy7YN91CJGOOsRQ");
        driver.get("http://localhost:3000/admin/");
        adminPage = new AdminPage(driver);
        adminPage.addProductBtn.click();
        addProductPage = new AddProductPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.get("http://localhost:3000/admin/");
        List<WebElement> newElements = driver.findElements(By.className("item"));
        while(newElements.size() > 0) {
            newElements.get(0).findElement(By.className("edit-product-btn")).click();
            driver.findElement(By.id("delete-btn")).click();
            driver.get("http://localhost:3000/admin/");
            newElements = driver.findElements(By.className("item"));
        }
        driver.quit();
    }

    public void clearUp() {
        driver.get("http://localhost:3000/admin/");
        List<WebElement> newElements = driver.findElements(By.className("item"));
        while(newElements.size() > 0) {
            newElements.get(0).findElement(By.className("edit-product-btn")).click();
            driver.findElement(By.id("delete-btn")).click();
            driver.get("http://localhost:3000/admin/");
            newElements = driver.findElements(By.className("item"));
        }
    }

    @Test
    void testAddProductPageVisibility() {
        assertTrue(addProductPage.nameInput.isDisplayed());
        assertTrue(addProductPage.categoryInput.isDisplayed());
        assertTrue(addProductPage.priceInput.isDisplayed());
        assertTrue(addProductPage.submitBtn.isDisplayed());
    }

    @Test
    void testAddProductPageUrl() {
        assertEquals(addProductPage.getUrl(), "http://localhost:3000/admin/add-item");
    }

    @Test
    void testEmptyAddProductPage() {
        driver.get("http://localhost:3000/admin");
        assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/div")).getText(), "No editable items");
    }

    @Test
    void testAddProduct() throws InterruptedException {
        addProductPage.nameInput.sendKeys("New product");
        addProductPage.categoryInput.sendKeys("New category");
        addProductPage.priceInput.sendKeys("150");
        addProductPage.submitBtn.click();
        Thread.sleep(5000);
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/");
        assertEquals(driver.findElements(By.className("item")).size(), 11);
        assertEquals(driver.findElements(By.className("item")).get(10).getText(), "New product\n150zł\nAdd to basket");
    }

    @Test
    void testAddProductWithFloatPrice() throws InterruptedException {
        addProductPage.nameInput.sendKeys("New product");
        addProductPage.categoryInput.sendKeys("New category");
        addProductPage.priceInput.sendKeys("149.99");
        addProductPage.submitBtn.click();
        Thread.sleep(5000);
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/");
        assertEquals(driver.findElements(By.className("item")).size(), 11);
        assertEquals(driver.findElements(By.className("item")).get(10).getText(), "New product\n149.99zł\nAdd to basket");
    }

    @Test
    void testNewItemVisibleInAdminPage() throws InterruptedException {
        addProductPage.nameInput.sendKeys("New product");
        addProductPage.categoryInput.sendKeys("New category");
        addProductPage.priceInput.sendKeys("150");
        addProductPage.submitBtn.click();
        Thread.sleep(2000);
        driver.get("http://localhost:3000/admin");
        Thread.sleep(2000);
        assertEquals(driver.findElements(By.className("item")).size(), 1);
    }

    @Test
    void testRemoveProduct() throws InterruptedException {
        addProductPage.nameInput.sendKeys("New product");
        addProductPage.categoryInput.sendKeys("New category");
        addProductPage.priceInput.sendKeys("150");
        addProductPage.submitBtn.click();
        Thread.sleep(2000);
        driver.get("http://localhost:3000/admin/");
        driver.findElement(By.className("edit-product-btn")).click();
        driver.findElement(By.id("delete-btn")).click();
        Thread.sleep(2000);
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/");
        Thread.sleep(2000);
        assertEquals(driver.findElements(By.className("item")).size(), 10);
    }

    @Test
    void testAddMultipleProducts() throws InterruptedException {
        addProductPage.nameInput.sendKeys("New product 1");
        addProductPage.categoryInput.sendKeys("New category 1");
        addProductPage.priceInput.sendKeys("150");
        addProductPage.submitBtn.click();
        Thread.sleep(5000);
        driver.get("http://localhost:3000/admin");
        adminPage.addProductBtn.click();
        addProductPage.nameInput.sendKeys("New product 2");
        addProductPage.categoryInput.sendKeys("New category 2");
        addProductPage.priceInput.sendKeys("150");
        addProductPage.submitBtn.click();
        Thread.sleep(5000);
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/");
        assertEquals(driver.findElements(By.className("item")).size(), 12);
        assertEquals(driver.findElements(By.className("item")).get(10).getText(), "New product 1\n150zł\nAdd to basket");
        assertEquals(driver.findElements(By.className("item")).get(11).getText(), "New product 2\n150zł\nAdd to basket");
    }

    @Test
    void testEditProduct() throws InterruptedException {
        addProductPage.nameInput.sendKeys("New product");
        addProductPage.categoryInput.sendKeys("New category");
        addProductPage.priceInput.sendKeys("150");
        addProductPage.submitBtn.click();
        Thread.sleep(5000);
        driver.get("http://localhost:3000/admin");
        driver.findElement(By.className("edit-product-btn")).click();
        driver.findElement(By.id("name-input")).sendKeys("Modified name");
        driver.findElement(By.id("category-input")).sendKeys("Modified category");
        driver.findElement(By.id("price-input")).sendKeys("999");
        driver.findElement(By.id("submit-btn")).click();
        Thread.sleep(5000);
        assertEquals(driver.findElements(By.className("item")).size(), 11);
        assertEquals(driver.findElements(By.className("item")).get(10).getText(), "Modified name\n999zł\nAdd to basket");
    }

    @Test
    void testAddNewProductToBasket() throws InterruptedException {
        addProductPage.nameInput.sendKeys("New product");
        addProductPage.categoryInput.sendKeys("New category");
        addProductPage.priceInput.sendKeys("150");
        addProductPage.submitBtn.click();
        Thread.sleep(5000);
        driver.findElements(By.className("item")).get(10).findElement(By.xpath(".//div/div[3]")).click();
        assertTrue(driver.findElement(By.xpath("//*[@id=\"root\"]/div/a/div")).isDisplayed());
    }
}
