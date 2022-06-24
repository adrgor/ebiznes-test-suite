package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.MainPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;

class MainPageTest {
    private WebDriver driver;
    private MainPage mainPage;
    private String HOME_URL = "http://localhost:3000/";

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:3000/");

        mainPage = new MainPage(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testCurrentUrl() {
        assertEquals(mainPage.getUrl(), HOME_URL);
    }

    @Test
    void testMainPageVisibility() {
        assertTrue(mainPage.header.isDisplayed());
        assertTrue(mainPage.footer.isDisplayed());
        assertTrue(mainPage.loginButton.isDisplayed());
        assertTrue(mainPage.itemList.isDisplayed());
    }

    @Test
    void testLoggedOutPage() {
        assertEquals(mainPage.loginButton.getText(), "Login");
        assertEquals(mainPage.itemList.findElements(By.tagName("div")).size(), 2);
    }

    @Test
    void testLoginButton() {
        mainPage.loginButton.click();
        assertEquals(driver.getCurrentUrl(), HOME_URL + "login");
    }

    @Test
    void testHomeButton() {
        mainPage.homeButton.click();
        assertEquals(driver.getCurrentUrl(), HOME_URL);
        mainPage.loginButton.click();
        mainPage.homeButton.click();
        assertEquals(driver.getCurrentUrl(), HOME_URL);
    }

    @Test
    void testBasketButton() {
        mainPage.basketButton.click();
        assertEquals(driver.getCurrentUrl(), HOME_URL + "basket");
    }

    @Test
    void testNotFoundPage() {
        driver.get("http://localhost:3000/random-url-that-doenst-exist");
        assertEquals(driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]")).getText(), "404");
    }
}
