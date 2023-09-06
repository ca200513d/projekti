package rs.etf.tests;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */

import static com.mycompany.domacits.DomaciTS.baseURL;
import static com.mycompany.domacits.DomaciTS.driver;
import static com.mycompany.domacits.DomaciTS.uspjesnaRegistracijaTest;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
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
public class TP3Registracija {
    
    
    public TP3Registracija() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void RegistracijaTest() {
         //potrebno zbog korelacije sa web browserom 
        System.setProperty("webdriver.chrome.driver","C:\\Users\\user2\\Desktop\\projekat3.7\\chromedriver.exe");
        String baseURL="http://localhost/projekatIP";
        ChromeDriver driver =new ChromeDriver();
        driver.get(baseURL);//dohvatanje sajta za testiranje
         driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/a[1]")).click();
        driver.findElement( By.xpath("/html/body/div/table/tbody/tr/td[2]/form/center/center/input")).click();
       
        String kime="anjacuric04";
        String sifra="Anja1234.";
        String ime="Anja";
        String prezime="Curic";
        String brtel="065 065 065";
        String email="anjacuric1@gmail.com";
        String god="3";
        String slika="C:\\Users\\user2\\Destkop\\projekat3.7\\slikaTest.png";
        driver.findElement(By.name("kime")).sendKeys(kime);//pronalazenje elementa sa stranice i slanje elementu odredjene sekvence

        
        driver.findElement(By.name("sifra")).sendKeys(sifra);
 
        
        driver.findElement(By.name("ime")).sendKeys(ime);

       
        driver.findElement(By.name("prezime")).sendKeys(prezime);

        
        driver.findElement(By.name("telefon")).sendKeys(brtel);

        
        driver.findElement(By.name("mejl")).sendKeys(email);

        
        driver.findElement(By.name("godstudija")).sendKeys(god);
       
        
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/form/input[9]")).click();
       
       WebElement elem=driver.findElement(By.name("slika"));
       
            
        File file = new File(System.getProperty("user.dir") + File.separator + "slikaTest.png" );

        String imagePath = file.getAbsolutePath();
        elem.sendKeys(imagePath);
        driver.findElement(By.name("Registruj")).click();
      
        
         try{
             //verifikaciona tacka
            String poruka=(String)driver.findElement(By.id("regstudent1")).getText();
             Assert.assertTrue(poruka.contains("Uspesno ste se"));
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
