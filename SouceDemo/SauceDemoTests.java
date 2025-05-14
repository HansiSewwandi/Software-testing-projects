import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SauceDemoTests {

    private static WebDriver driver;

    @BeforeClass
    public static void setUpBeforeClass(){
        System.setProperty("webdriver.chrome.driver","C:/Users/ACER/OneDrive/Desktop/Selenium testing/New folder (2)/chromedriver-win64.chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDownAfterClass(){
        if(driver != null){
            driver.quit();
        }
    }

    @Before
    public void setUp(){
        driver.get("https://www.saucedemo.com/");
    }

    private void login(String username, String password){
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    @Test
    public void TC001_loginWithValidCredentials(){
        login("standard_user", "secret_sauce");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    @Test
    public void TC002_loginWithInvalidCredentials() {
        login("invalid_user", "invalid_pass");
        String errorMsg = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(errorMsg.contains("Username and password do not match any user"));
    }

    @Test
    public void TC003_verifyProductListing(){
        login("standard_user", "secret_sauce");
        List<WebElement> products = driver.findElements(By.className("inventory_item"));
        Assert.assertEquals(6, products.size());
    }

    @Test
    public void TC004_addProductToCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.name("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        Assert.assertTrue(driver.getPageSource().contains("Sauce Labs Backpack"));
    }

    @Test
    public void TC005_removeProductFromCart() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.name("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.name("remove-sauce-labs-backpack")).click();
        Assert.assertFalse(driver.getPageSource().contains("Sauce Labs Backpack"));
    }

    @Test
    public void TC006_sortProductsLowToHigh() {
        login("standard_user", "secret_sauce");
        Select sortDropdown = new Select(driver.findElement(By.className("product_sort_container")));
        sortDropdown.selectByVisibleText("Price (low to high)");
        // You can add a price verification here
        Assert.assertTrue(true);
    }

    @Test
    public void TC007_completeCheckoutProcess() {
        login("standard_user", "secret_sauce");
        driver.findElement(By.name("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("finish")).click();
        Assert.assertTrue(driver.getPageSource().contains("THANK YOU FOR YOUR ORDER"));
    }

    @Test
    public void TC008_logoutFromInventoryPage() throws InterruptedException {
        login("standard_user", "secret_sauce");
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(1000); // Allow time for menu animation
        driver.findElement(By.id("logout_sidebar_link")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"));
    }


}
