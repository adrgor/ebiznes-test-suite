package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EditProductPageTest {

    private WebDriver driver;
    private EditProductPage editProductPage;

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
        driver.get("http://localhost:3000/admin/edit-item/1");
        editProductPage = new EditProductPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testEditProductVisibility() {
        assertTrue(editProductPage.nameInput.isDisplayed());
        assertTrue(editProductPage.categoryInput.isDisplayed());
        assertTrue(editProductPage.priceInput.isDisplayed());
        assertTrue(editProductPage.submitBtn.isDisplayed());
        assertTrue(editProductPage.deleteBtn.isDisplayed());
    }

    @Test
    void testEditProductUrl() {
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/admin/edit-item/1");
    }
}
