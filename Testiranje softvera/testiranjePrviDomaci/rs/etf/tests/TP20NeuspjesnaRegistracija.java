/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package rs.etf.tests;

import org.openqa.selenium.By;
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
public class TP17NeuspjesnaRegistracija {
    
    public TP17NeuspjesnaRegistracija() {
    }

     @Test
    public void RegistracijaTest() {
         //potrebno zbog korelacije sa web browserom 
        System.setProperty("webdriver.chrome.driver","C:\\Users\\user2\\Desktop\\projekat3.7\\chromedriver.exe");
        String baseURL="http://localhost/projekatIP";
        ChromeDriver driver =new ChromeDriver();
        driver.get(baseURL);//dohvatanje sajta za testiranje
         driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/a[1]")).click();
        driver.findElement( By.xpath("/html/body/div/table/tbody/tr/td[2]/form/center/center/input")).click();
       
        String kime="anja_curic";
        String sifra="AVanja#67";
        String ime="Anja";
        String prezime="Curic";
        String brtel="059/280-280";
        String email="anjacuric1@gmail.com";
        String god="4";
        
        driver.findElement(By.name("kime")).sendKeys(kime);//pronalazenje elementa sa stranice i slanje elementu odredjene sekvence

        
        driver.findElement(By.name("sifra")).sendKeys(sifra);
 
        
        driver.findElement(By.name("ime")).sendKeys(ime);

       
        driver.findElement(By.name("prezime")).sendKeys(prezime);

        
        driver.findElement(By.name("telefon")).sendKeys(brtel);

        
        driver.findElement(By.name("mejl")).sendKeys(email);

        
        driver.findElement(By.name("godstudija")).sendKeys(god);
       
        
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/form/input[8]")).click();
       
        driver.findElement(By.name("Registruj")).click();
        
        
          try{
             //verifikaciona tacka
            String text=driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            Assert.assertTrue(text.contains(""));
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
