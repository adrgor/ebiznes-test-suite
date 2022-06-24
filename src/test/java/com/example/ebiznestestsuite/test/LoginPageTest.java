package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.LoginPage;
import com.example.ebiznestestsuite.pom.MainPage;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPageTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private String LOGIN_URL = "http://localhost:3000/login";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:3000/login");

        loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testCurrentUrl() {
        assertEquals(loginPage.getUrl(), LOGIN_URL);
    }

    @Test
    void testLoginPageVisibility() {
        assertTrue(loginPage.loginForm.isDisplayed());
        assertTrue(loginPage.googleLoginButton.isDisplayed());
        assertTrue(loginPage.githubLoginButton.isDisplayed());
    }

    @Test
    void testGithubLoginRedirection() {
        loginPage.githubLoginButton.click();
        assertTrue(driver.getCurrentUrl().matches("https:\\/\\/github\\.com\\/login\\?client_id=.*"));
    }
}