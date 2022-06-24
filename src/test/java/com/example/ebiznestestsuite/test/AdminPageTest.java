package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.AdminPage;
import com.example.ebiznestestsuite.pom.CheckoutPage;
import com.example.ebiznestestsuite.pom.PaymentPage;
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

public class AdminPageTest {

    private WebDriver driver;
    private AdminPage adminPage;

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
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testAdminPageVisibility() {
        assertTrue(adminPage.addProductBtn.isDisplayed());
    }

    @Test
    void testAdminPageUrl() {
        assertEquals(adminPage.getUrl(), "http://localhost:3000/admin/");
    }
}
