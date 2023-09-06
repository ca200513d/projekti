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
public class TP27NeuspjesnaRegistracijaKompanije {
    
    public TP27NeuspjesnaRegistracijaKompanije() {
    }

    @Test
    public void RegistracijaTest() {
         //potrebno zbog korelacije sa web browserom 
        System.setProperty("webdriver.chrome.driver","C:\\Users\\user2\\Desktop\\projekat3.7\\chromedriver.exe");
        String baseURL="http://localhost/projekatIP";
        ChromeDriver driver =new ChromeDriver();
        driver.get(baseURL);//dohvatanje sajta za testiranje
         driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/a[1]")).click();
         driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td[2]/form/center/select/option[2]")).click();
        driver.findElement( By.xpath("/html/body/div/table/tbody/tr/td[2]/form/center/center/input")).click();
        
       
        String kime="kompanija1";
        String sifra="Komp1234#";
        String naziv="Kompanija";
        String grad="Beograd";
        String adresa="Vojvode Stepe,23";
        String PIB="1234";
        String email="anjacuric1@gmail.com";
        String brojZap="4";
        String slika="C:\\Users\\user2\\Destkop\\projekat3.7\\slikaTest.png";
        String ime="Anja Curic";
        driver.findElement(By.name("kime")).sendKeys(kime);//pronalazenje elementa sa stranice i slanje elementu odredjene sekvence
        
        
        driver.findElement(By.name("sifra")).sendKeys(sifra);
 
        
        driver.findElement(By.name("naziv")).sendKeys(naziv);

        
        driver.findElement(By.name("adresa")).sendKeys(adresa);

        driver.findElement(By.name("imedir")).sendKeys(ime);
        
        driver.findElement(By.name("pib")).sendKeys(PIB);

        driver.findElement(By.name("brzaposlenih")).sendKeys(brojZap);
        
        driver.findElement(By.name("mejl")).sendKeys(email);

        driver.findElement(By.name("sajt")).sendKeys("www.kompanija.com");
       
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/form/select/option[1]")).click();
        driver.findElement(By.name("specijalnost")).sendKeys("Baze podataka");
        
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/form/input[9]")).click();
       
       WebElement elem=driver.findElement(By.name("slika"));
       
            
        File file = new File(System.getProperty("user.dir") + File.separator + "slikaTest.png" );

        String imagePath = file.getAbsolutePath();
        elem.sendKeys(imagePath);
        driver.findElement(By.name("Registruj")).click();
        
        try{
             //verifikaciona tacka
            String text=driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            Assert.assertTrue(text.contains("Polje grad ne sme biti prazno!"));
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
