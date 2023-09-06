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
public class TP21Logovanje {
    
    public TP21Logovanje() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
   @Test
   public void TestLogovanje(){ 
        System.setProperty("webdriver.chrome.driver","C:\\Users\\user2\\Desktop\\projekat3.7\\chromedriver.exe");
        String baseURL="http://localhost/projekatIP";
        ChromeDriver driver =new ChromeDriver();
        driver.get(baseURL);//dohvatanje sajta za testiranje
        String kime="aleksa";
        String sifra="Sifra1234$";
        driver.findElement(By.name("user")).sendKeys(kime);
        driver.findElement(By.name("pass")).sendKeys(sifra);
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/form/center/input")).click();
        String poruka=(String)driver.findElement(By.id("dobrodosao")).getText();
          try{
             //verifikaciona tacka
            Assert.assertTrue(poruka.contains("Dobrodosao Aleksa Aleksic"));
            //za error message Assert.assertEquals(poruka,nesto) 
           
            
         }
         catch(Throwable e){ 
             System.out.println(e.getMessage());
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
