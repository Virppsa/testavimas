package org.example;

import dev.failsafe.internal.util.Assert;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Testas4 {

    //private static String email_register = "user-438654255595719931@test.com";


    private static final String baseUrl = "https://demowebshop.tricentis.com/";
    private static String email = "";
    private static final String pass = "Pasiss123$";
    private static ChromeDriver driver;


    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before class");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);

        email =  String.format("user%d%d@test.com", new Random().nextInt(), new Random().nextInt());

        //1. Atsidaryti tinklalapi https://demowebshop.tricentis.com/
        driver.get(baseUrl);

        //2. Spausti 'Log in'
        driver.findElement(By.xpath("//a[@class='ico-login' and @href='/login']")).click();

        //3. Spausti 'Register' skiltyje 'New Customer'
        driver.findElement(By.xpath("//input[@type='button' and @class='button-1 register-button' and @value='Register']")).click();

        //4. Upildyti registracijos formos laukus
        driver.findElement(By.id("gender-female")).click();
        driver.findElement(By.id("FirstName")).sendKeys("Tester");
        driver.findElement(By.id("LastName")).sendKeys("Tester");
        driver.findElement(By.id("Email")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(pass);
        driver.findElement(By.id("ConfirmPassword")).sendKeys(pass);

        //5. Spausti 'Register'
        driver.findElement(By.id("register-button")).click();

        //6. Spausti 'Continue'
        driver.findElement(By.xpath("//input[@type='button' and @class='button-1 register-continue-button' and @value='Continue']")).click();

        //uzdarome draiverį
        driver.quit();
    }


    @Before
    public void beforeEach() {
        System.out.println("Before Each");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        //driver = new ChromeDriver();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void testScenario1() {
        performScenario2("data1.txt");
    }

    @Test
    public void testScenario2() {
        performScenario2("data2.txt");
    }

    //Uždarom draiverį
    @After
    public void afterEach() {
        System.out.println("After Each");
        if (driver != null) {
            driver.quit();
        }
    }
    private static void performScenario2(String dataFile) {

        //1. Atsidaryti tinklalapj https://demowebshop.tricentis.com/
        driver.get(baseUrl);

        //2. Spausti 'Log in'
        driver.findElement(By.xpath("//a[@href='/login']")).click();

        //3. Upildyti 'Email:, 'Password:" ir spausti 'Log in'
        WebElement emailElement = driver.findElement(By.id("Email"));
        emailElement.click();
        emailElement.sendKeys(email);

        WebElement passwordElement = driver.findElement(By.id("Password"));
        passwordElement.click();
        passwordElement.sendKeys(pass);

        driver.findElement(By.cssSelector("input[value='Log in']")).click();

        //4. Šoniniame meniu pasirinkti 'Digital downloads'
        driver.findElement(By.xpath("//a[@href='/digital-downloads']")).click();


        //5. Prideti i krepseli prekes nuskaitant testini faila (pirmam testui skaityti is datal.txt, antram testui skaityti is data2.txt)
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFile));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(30000));
            for (String line : lines) {
                WebElement element = driver.findElement(By.xpath("//div[@class='item-box']//h2[@class='product-title']/a[text() = '" + line + "']/ancestor::div[@class='product-item']//input[@type='button'] "));
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //6. Atsidaryti 'Shopping cart'
        driver.findElement(By.cssSelector("span.cart-label")).click();

        //7. Paspausti 'I agree' varnele ir mygtuka 'Checkout'
        driver.findElement(By.id("termsofservice")).click();
        driver.findElement(By.id("checkout")).click();


        //8. 'Billing Address' pasirinkti jau esanti adresa arba supildyti naujo adreso laukus, spausti 'Continue'
        try {
            WebElement billingAddressSelect = driver.findElement(By.id("billing-address-select"));
            if (billingAddressSelect.isDisplayed()) {
                System.out.println("Billing address select found...");
                billingAddressSelect.click();
                billingAddressSelect.sendKeys("n");
                billingAddressSelect.sendKeys(Keys.ENTER);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Billing address select is not found...");
        }

        WebElement countryElement = driver.findElement(By.id("BillingNewAddress_CountryId"));
        countryElement.sendKeys(Keys.ENTER);
        countryElement.sendKeys("lit");
        countryElement.sendKeys(Keys.ENTER);

        driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Kažkokia");
        driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("Kažkokia g. 10");
        driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("123");
        driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("123");


        driver.findElement(By.cssSelector("input[type='button'][title='Continue'][class='button-1 new-address-next-step-button']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //9. 'Payment Method' spausti 'Continue'
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[type='button'].button-1.payment-method-next-step-button"))).click();

        //10. 'Payment Information' spausti 'Continue'
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.button-1.payment-info-next-step-button"))).click();

        //11. 'Confirm Order' spausti 'Confirm'
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.button-1.confirm-order-next-step-button"))).click();

        //12. Isitikinti, kad uzsakymas sékmingai uzskaitytas.
        Assert.isTrue(elementExists(driver, "//strong[text()='Your order has been successfully processed!']"), "Order did not go through");
    }

    public static boolean elementExists(WebDriver driver, String xpath) {
        try {
            driver.findElement(By.xpath(xpath));
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

}