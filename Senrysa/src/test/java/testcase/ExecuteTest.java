package testcase;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.DataFormatter;

public class ExecuteTest{
	public WebDriver driver;
	public static HSSFWorkbook workbook;
    public static HSSFSheet worksheet;
    public static DataFormatter formatter= new DataFormatter();
   
 
	@BeforeSuite
	public void setup() {
		System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\selenium\\geckodriver.exe");
		WebDriver driver=new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get("http://nextdoorhub.com");
	    
	    WebDriver wait = (WebDriver) new WebDriverWait(driver,30);
	   
	   driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}  
	   @BeforeTest
	   public void assertTitle() {
		   
	   String expectedTitle= "nextdoorhub";
	    String actualTitle= driver.getTitle();
	    if(actualTitle==expectedTitle) {
	    	 Assert.assertEquals(actualTitle,expectedTitle);
	    	 System.out.println("PASS");
	    }
	    else {
	    	 System.out.println("Fail");

	    }
	}
	@DataProvider
    public static Object[][] ReadVariant() throws IOException
    {
    FileInputStream fileInputStream= new FileInputStream("C:\\Users\\user\\Desktop\\Demodata.xlsx"); //Excel sheet file location get mentioned here
        HSSFWorkbook workbook = new HSSFWorkbook (fileInputStream); //get my workbook 
        HSSFSheet worksheet=workbook.getSheet("testdata");// get my sheet from workbook
        HSSFRow Row=worksheet.getRow(0);     //get my Row which start from 0   
     
        int RowNum = worksheet.getPhysicalNumberOfRows();// count my number of Rows
        int ColNum= Row.getLastCellNum(); // get last ColNum 
         
        Object Data[][]= new Object[RowNum-1][ColNum]; // pass my  count data in array
         
            for(int i=0; i<RowNum-1; i++) //Loop work for Rows
            {  
                HSSFRow row= worksheet.getRow(i+1);
                 
                for (int j=0; j<ColNum; j++) //Loop work for colNum
                {
                    if(row==null)
                        Data[i][j]= "";
                    else
                    {
                        HSSFCell cell= row.getCell(j);
                        if(cell==null)
                            Data[i][j]= ""; //if it get Null value it pass no data 
                        else
                        {
                            String value=formatter.formatCellValue(cell);
                            Data[i][j]=value; //This formatter get my all values as string i.e integer, float all type data value
                        }
                    }
                }
            }
 
        return Data;
    }

	@Test(dataProvider="ReadVariant")
	public void addCred(String Username,String Password) {
		driver.findElement(By.xpath("//*[@class='btn btn-outline-secondary btn-md']")).click();
		driver.findElement(By.xpath("//*[@placeholder=' Enter Email/Mobile Number*']")).sendKeys(Username);
		driver.findElement(By.xpath("//*[@type='password']")).sendKeys(Password);
		driver.findElement(By.xpath("//*[@type='submit']")).click();	
	}
	
	@AfterTest
	public void steps() {
		driver.get("https://www.google.com");
		driver.navigate().back();
		System.out.println(driver.getCurrentUrl());
		driver.navigate().forward();
		driver.navigate().refresh();
		driver.close();
		driver.quit();
		
	}
}