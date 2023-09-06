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
public class TP33NuspjesnaPromjenaLozinke {
    
    public TP33NuspjesnaPromjenaLozinke() {
    }

    @Test
    public void TestPromjena(){ 
         System.setProperty("webdriver.chrome.driver","C:\\Users\\user2\\Desktop\\projekat3.7\\chromedriver.exe");
        String baseURL="http://localhost/projekatIP";
        ChromeDriver driver =new ChromeDriver();
        driver.get(baseURL);//dohvatanje sajta za testiranje
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/a[2]")).click();
        
        String ime="jeca";
        String sifra="Sifra1234$";
        String nova="Anja12.";
        driver.findElement(By.name("user")).sendKeys(ime);
        driver.findElement(By.name("passstari")).sendKeys(sifra);
        driver.findElement(By.name("sifra")).sendKeys(nova);
        driver.findElement(By.name("potvrdi")).click();
        
        
         try{
              String text=driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            Assert.assertTrue(text.contains("Sifra mora imati od 8 do 12 karaktera"));
            if(driver!=null)driver.quit();
            
         }
         catch(Throwable e){ 
             //verifikaciona tacka
            String poruka=(String)driver.findElement(By.id("regstudent1")).getText();
             Assert.assertTrue(poruka.contains("Uspesno ste promenili sifru!\n" +
"Vratite se na stranicu za logovanje OVDE"));
            //za error message Assert.assertEquals(poruka,nesto) 
         }
        if(driver!=null)driver.quit();
        
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
