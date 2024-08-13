import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondTest {

    WebDriver driver;

    @Test
    public void secondTest() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://demo.dealsdray.com/");
        driver.manage().window().maximize();
        driver.findElement(By.name("username")).sendKeys("prexo.mis@dealsdray.com");
        driver.findElement(By.name("password")).sendKeys("prexo.mis@dealsdray.com");
        driver.findElement(By.xpath("//*[text()='Login']")).click();
        Thread.sleep(2000);
        //                                                          class="MuiButtonBase-root has-submenu compactNavItem css-46up3a"

        WebElement we  = driver.findElement(By.xpath("//button[contains(@class,'MuiButtonBase-root has-submenu compactNavItem css-46up3a')]"));
        if(!we.getAttribute("class").contains("open")){
            driver.findElement(By.xpath("//button[contains(@class,'MuiButtonBase-root has-submenu compactNavItem css-46up3a')]")).click();
        }
        driver.findElement(By.xpath("//span[text()='Orders']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[text()='Add Bulk Orders']")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("mui-7")).sendKeys(System.getProperty("user.dir")+"\\src\\main\\resources\\demo-data.xlsx");
        driver.findElement(By.xpath("//*[text()='Import']")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[text()='Validate Data']")).click();
        Thread.sleep(2000);
        Alert alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(2000);
        takeScreenshot();
        driver.close();

    }

    public void takeScreenshot()  {
        TakesScreenshot scrShot =((TakesScreenshot)driver);
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyHHmmss");
        String currentDateFormat = simpleDateFormat.format(date);
        File destFile=new File(System.getProperty("user.dir")+"\\src\\main\\resources\\screenshots\\"+currentDateFormat+".jpg");
        try {
            FileUtils.copyFile(SrcFile, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
