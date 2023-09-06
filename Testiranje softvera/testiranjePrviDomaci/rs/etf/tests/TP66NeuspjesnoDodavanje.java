/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package rs.etf.tests;

import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author user2
 */
public class TP46NeuspjesnoDodavanje {
    
    public TP46NeuspjesnoDodavanje() {
    }

    @Test 
    public void TestDodavanje(){ 
         //potrebno zbog korelacije sa web browserom 
        System.setProperty("webdriver.chrome.driver","C:\\Users\\user2\\Desktop\\projekat3.7\\chromedriver.exe");
        String baseURL="http://localhost/projekatIP";
        ChromeDriver driver =new ChromeDriver();
        driver.get(baseURL);//dohvatanje sajta za testiranje
         String kime="stevica";
        String sifra="Admin123.";
        String naziv="JobFair3";
        String datumOd="24/12/2022";
        String datumDo="12/01/2023";
        driver.findElement(By.name("user")).sendKeys(kime);
        driver.findElement(By.name("pass")).sendKeys(sifra);
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/form/center/input")).click();
        driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td[3]/a")).click();
        driver.findElement(By.name("naziv")).sendKeys(naziv);
        driver.findElement( By.name("datumO")).sendKeys(datumOd);
        driver.findElement( By.name("datumZ")).sendKeys(datumDo);
        driver.findElement( By.xpath("/html/body/table[2]/tbody/tr/td[2]/form/center/input")).click();
        String datum="26/12/2022";
        String vrijemedo="20:00";
        String sala="70";
        WebElement elem=driver.findElement(By.name("logo"));
       
            
        File file = new File(System.getProperty("user.dir") + File.separator + "slikaTest.png" );

        String imagePath = file.getAbsolutePath();
        elem.sendKeys(imagePath);
        
        driver.findElement(By.name("datum")).sendKeys(datum);
        driver.findElement(By.name("vremeK")).sendKeys(vrijemedo);
        driver.findElement(By.name("prostorija")).sendKeys(sala);
        driver.findElement(By.name("potvrdi")).click();
        
         try{
             //verifikaciona tacka
            String text=driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            Assert.assertTrue(text.contains("Polje vreme pocetka ne sme"));
            text=driver.switchTo().alert().getText();
            if(driver!=null)driver.quit();
            
         }
         catch(Throwable e){ 
             //ako ne postoji nijedan alert,onda prolazi kako treba
             String poruka=(String)driver.findElement(By.id("regstudent1")).getText();
             Assert.assertTrue(poruka.contains("Uspesno ste se"));
             if(driver!=null)driver.quit();
         }
        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
