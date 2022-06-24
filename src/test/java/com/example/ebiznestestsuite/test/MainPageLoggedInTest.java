package com.example.ebiznestestsuite.test;

import com.example.ebiznestestsuite.pom.MainPage;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainPageLoggedInTest {

    private WebDriver driver;
    private MainPage mainPage;
    private MainPageLoggedIn mainPageLoggedIn;
    private String HOME_URL = "http://localhost:3000/";

    Set<String> details = new HashSet<String>();

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
        driver.get("http://localhost:3000/");

        details.add("Xiaomi Redmi Note 11 4/64GB Twilight Blue=>899zł");
        details.add("Dell P2422H=>1099.99zł");
        details.add("Huawei MateBook D 15-R5-5500U/8GB/512/Win11=>2999zł");
        details.add("Xiaomi Redmi 9A 2/32GB=>399zł");
        details.add("Philips 50PUS8546=>2999zł");
        details.add("Nintendo Switch=>1349zł");
        details.add("Acer Nitro VG240YBMIIX=>649zł");
        details.add("Dell Inspiron 5515 Ryzen 5 5500U/16GB/512/Win11=>3399zł");
        details.add("Apple MacBook Air M1/8GB/256=>4599zł");
        details.add("realme 8 4+64GB=>999zł");

        mainPage = new MainPage(driver);
        mainPageLoggedIn = new MainPageLoggedIn(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void testItemsCount() throws InterruptedException {
        Thread.sleep(1000);
        assertEquals(10, driver.findElements(By.className("item")).size());
    }

    @Test
    void testItemContent() {
        List<WebElement> items = driver.findElements(By.className("item"));
        Set<String> itemDetails = items.stream().map(item -> {
            List<WebElement> details = item.findElement(By.className("details")).findElements(By.tagName("div"));
            String name = details.get(0).getText();
            String price = details.get(1).getText();
            return name + "=>"+price;
        }).collect(Collectors.toSet());
        assertEquals(itemDetails, details);
    }

    @Test
    void testLogoutButton() {
        assertEquals("Logout", mainPage.loginButton.getText());
    }

    @Test
    void testLogout() {
        mainPage.loginButton.click();
        assertEquals(driver.findElements(By.className("item")).size(), 0);
        assertEquals("http://localhost:3000/", driver.getCurrentUrl());
        assertEquals("Login", mainPage.loginButton.getText());
    }

    @Test
    void testGoToBasketNoDisplayed() {
        assertEquals(driver.findElements(By.xpath("//*[@id=\"root\"]/div/a")).size(), 0);
    }

    @Test
    void testAddToBasket() {
        WebElement addToBasket = mainPageLoggedIn.items.get(0).findElement(By.xpath("//div[3]"));
        addToBasket.click();
        assertTrue(driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).isDisplayed());
        assertEquals("Go to basket (1)>", driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).getText());
    }

    @Test
    void testAddSameItemTwice() {
        WebElement addToBasket = mainPageLoggedIn.items.get(0).findElement(By.xpath("//div[3]"));
        addToBasket.click();
        addToBasket.click();
        assertEquals("Go to basket (2)>", driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).getText());
    }

    @Test
    void testAddDifferentItems() {
        WebElement addToBasket = mainPageLoggedIn.items.get(0).findElement(By.xpath("//div[3]"));
        addToBasket.click();
        addToBasket = mainPageLoggedIn.items.get(1).findElement(By.xpath("//div[3]"));
        addToBasket.click();
        assertEquals("Go to basket (2)>", driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).getText());
    }

    @Test
    void testAddEveryItem() {
        mainPageLoggedIn.items.forEach(item -> {
            item.findElement(By.xpath("//div[3]")).click();
        });
        assertEquals("Go to basket (10)>", driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).getText());
    }

    @Test
    void testGoToBasketButton() {
        WebElement addToBasket = mainPageLoggedIn.items.get(0).findElement(By.xpath("//div[3]"));
        addToBasket.click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/a")).click();
        assertEquals("http://localhost:3000/basket", driver.getCurrentUrl());
    }

    @Test
    void testResetBasket() {
        WebElement addToBasket = mainPageLoggedIn.items.get(0).findElement(By.xpath("//div[3]"));
        addToBasket.click();
        driver.get("http://localhost:3000");
        assertEquals(driver.findElements(By.xpath("//*[@id=\"root\"]/div/a")).size(), 0);
    }
}
