package testcases.com.bankguru.user;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Level_01_Repeat_Yourself {

    WebDriver driver;
    String projectPath = System.getProperty("user.dir");

    String userID , password , loginPageUrl;
    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", projectPath + "/browserDriver/chromedriver.exe");
        driver = new ChromeDriver();

        driver.get("https://demo.guru99.com/v4/index.php");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void TC_01_Register_To_System() {
        // Get login Page Url
        loginPageUrl = driver.getCurrentUrl();

        // click to here link
        driver.findElement(By.xpath("//a[normalize-space()='here']")).click();

        // Input to email textbox
        driver.findElement(By.xpath("//input[@name='emailid']")).sendKeys("ftf1234@gmail.com");

        // Click to Submit button
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        // Get User ID / Password information
        userID = driver.findElement(By.xpath("//td[normalize--space()='Password :']/following-sibling::td")).getText();
    }

    @Test
    public void TC_02_Login_To_System() {
        // Open Login Page
        driver.get(loginPageUrl);

        // Input to Username / Password to textbox
        driver.findElement(By.xpath("//input[@name='uid']")).sendKeys(userID);
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);

        // Click to Login button
        driver.findElement(By.xpath("//input[@name='btnLogin']")).click();

        // Navigate to Home Page
        Assert.assertEquals(driver.findElement(By.xpath("//marquee[@class='heading3']")).getText() ,
                "Welcome To Manager's Page of Guru99 Bank");
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}
