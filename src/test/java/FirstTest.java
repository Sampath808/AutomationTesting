
import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FirstTest  {

    WebDriver driver;
    private ATUTestRecorder recorder;

    @BeforeMethod
    public synchronized void beforeMethod(Method method) throws MalformedURLException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyHHmmss");
        try {
            recorder = new ATUTestRecorder(System.getProperty("user.dir")+"\\Videos\\",method.getName()+"_"+simpleDateFormat.format(date),false);
        } catch (ATUTestRecorderException e) {
            throw new RuntimeException(e);
        }
        try {
            recorder.start();
        } catch (ATUTestRecorderException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUpBrowser(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "safari":
                WebDriverManager.safaridriver().setup();
                driver = new SafariDriver();
                break;
        }

    }


    @Test(description = "screen Resolution Test",dataProvider = "screenResolution" )
    public void screenResolutionTest(int dimension1,int dimension2) throws InterruptedException {
        setUpBrowser("chrome");
        driver.get("https://www.getcalley.com/page-sitemap.xml");
        driver.manage().window().setSize(new Dimension(dimension1,dimension2));
        List<WebElement> links = driver.findElements(By.xpath("(//*[@id='sitemap']//a)[position()<6]"));
        for(int i=0; i< links.size(); i++){
            try {
                links.get(i).click();
                takeScreenshot();
                Thread.sleep(1000);
                driver.navigate().back();
                Thread.sleep(1000);
            }catch (StaleElementReferenceException staleElementReferenceException){
                links = driver.findElements(By.xpath("(//*[@id='sitemap']//a)[position()<3]"));
                links.get(i).click();
                takeScreenshot();
                Thread.sleep(2000);
                driver.navigate().back();
                Thread.sleep(2000);
            }
        }

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



    @DataProvider(name= "screenResolution")
    private Object[][] dp(){
        Object[][] data = new Object[2][2];
        data = new Object[][]{{1920,1080},
                {1366,768},
                {1536,864},
                {360,640},
                {414,896},
                {375, 667}};

        return data;

    }

    @AfterMethod
    public void afterMethod(ITestResult result){
        try {
            recorder.stop();
        } catch (ATUTestRecorderException e) {
            throw new RuntimeException(e);
        }
    }






}





