/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.klijent;



import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public final class Klijent {
  
  public static final String API_URL = "http://localhost:8080/server/";
  private static Semaphore sem=new Semaphore(0);
  private static String korisnickoIme=null;
  private static String lozinka=null;
  private static boolean kraj=false;

  public interface MyAPI {
    @GET("app/podsistem1/svi.korisnici/{kime}")
    Call<ResponseBody>getKorisnici(@Path("kime")String kime);
    
    @GET("app/podsistem1/svi.gradovi")
    Call<ResponseBody>getGradovi();
    
    @POST ("app/podsistem1/kreiraj.grad/{kime}/{Naziv}")
    Call<ResponseBody>createGrad(@Path("kime")String kime,@Path("Naziv")String naziv);
    
    @POST ("app/podsistem3/kreiraj.grad/{kime}/{Naziv}")
    Call<ResponseBody>createGrad1(@Path("kime")String kime,@Path("Naziv")String naziv);
    
    @POST("app/podsistem1/kreiraj.korisnik/{kime}/{sifra}/{ime}/{prezime}/{adresa}/{novac}/{grad}")
    Call<ResponseBody>createKorisnik(@Path("kime")String kime,@Path("sifra")String sifra,@Path("ime")String ime,@Path("prezime")String prezime,@Path("adresa")String adresa,@Path("novac")Double novac,@Path("grad")String naziv);
  
    @POST("app/podsistem2/kreiraj.korisnik/{kime}/{sifra}/{ime}/{prezime}/{novac}")
    Call<ResponseBody>createKorisnik2(@Path("kime")String kime,@Path("sifra")String sifra,@Path("ime")String ime,@Path("prezime")String prezime,@Path("novac")Double novac);
  
    @PUT("app/podsistem1/dodaj.novac/{korIme}/{kolicina}")
    Call<ResponseBody>addMoney(@Path("korIme")String korIme,@Path("kolicina")Double novac);
    
    @PUT("app/podsistem2/dodaj.novac/{korIme}/{kolicina}")
    Call<ResponseBody>addMoney2(@Path("korIme")String korIme,@Path("kolicina")Double novac);
    
    @PUT("app/podsistem3/dodaj.novac/{korIme}/{kolicina}")
    Call<ResponseBody>addMoney3(@Path("korIme")String korIme,@Path("kolicina")Double novac);
  
    @PUT("app/podsistem1/promjeni.adresu/{Adresa}/{NazivGrada}/{KorIme}")
    Call<ResponseBody>changeAddress(@Path("Adresa")String adresa,@Path("NazivGrada")String grad,@Path("KorIme")String korime);
    
    @PUT("app/podsistem3/promjeni.adresu/{Adresa}/{NazivGrada}/{KorIme}")
    Call<ResponseBody>changeAddress1(@Path("Adresa")String adresa,@Path("NazivGrada")String grad,@Path("KorIme")String korime);
    
    @POST("app/podsistem2/dodaj.kategoriju/{kime}/{nazivKat}")
    Call<ResponseBody>dodajKat(@Path("kime")String kime,@Path("nazivKat")String nazivKat,@Query("nazivNadKat")String nazivNadKat);
    
    @POST("app/podsistem2/kriraj.artikal/{kime}/{naziv}/{cijena}/{popust}/{kategorija}")
    Call<ResponseBody>dodajArtikal(@Path("kime")String kime,@Path("naziv")String naziv,@Path("cijena")Double cijena,@Path("popust")Double popust,@Path("kategorija")String kat,@Query("opis")String opis);
    
    @PUT("app/podsistem2/mijenjaj.cijenju/{kime}/{idPro}/{cijena}")
    Call<ResponseBody>mijenjajCijenu(@Path("kime")String kime,@Path("idPro")int idPro,@Path("cijena")Double cijena);
    
    @PUT("app/podsistem3/mijenjaj.cijenju/{kime}/{idPro}/{cijena}")
    Call<ResponseBody>mijenjajCijenu1(@Path("kime")String kime,@Path("idPro")int idPro,@Path("cijena")Double cijena);
    
    @PUT ("app/podsistem2/mijenjaj.popust/{kime}/{idPro}/{popust}")
    Call<ResponseBody>mijenjajPopust(@Path("kime")String kime,@Path("idPro")int idPro,@Path("popust")Double popust);
    
    @PUT ("app/podsistem3/mijenjaj.popust/{kime}/{idPro}/{popust}")
    Call<ResponseBody>mijenjajPopust1(@Path("kime")String kime,@Path("idPro")int idPro,@Path("popust")Double popust);
  
    @PUT("app/podsistem2/dodaj.korpa/{kime}/{idPro}/{kolicina}")
    Call<ResponseBody>dodajKorpa(@Path("kime")String kime,@Path("idPro")int idPro,@Path("kolicina")int kolicina);
  
    @DELETE("app/podsistem2/izbaci.korpa/{kime}/{idPro}/{kolicina}")
    Call<ResponseBody>izbaciKorpa(@Path("kime")String kime,@Path("idPro")int idPro,@Path("kolicina")int kolicina);
    
    @GET("app/podsistem2/dohvati.kategorije/{kime}")
    Call<ResponseBody>dohvKategorije(@Path("kime")String kime);
    
    @GET("app/podsistem2/dohvati.prodaje/{kime}")
    Call<ResponseBody>dohvProdaje(@Path("kime")String kime);
    
    @GET("app/podsistem2/dohvati.korpa/{kime}")
    Call<ResponseBody>dohvKorpa(@Path("kime")String kime);
    
    @GET("app/podsistem3/dohvati.narudzbine/{kime}")
    Call<ResponseBody>getNarudzbina(@Path("kime")String kime);
    
    @GET("app/podsistem3/dohvati.narudzbine2/{kime}")
    Call<ResponseBody>getNarudzbina2(@Path("kime")String kime);
    
    @GET("app/podsistem3/dohvati.transakcije/{kime}")
    Call<ResponseBody>getTransakcije(@Path("kime")String kime);
    
    @GET("app/podsistem3/plati/{kime}/{sifra}")
    Call<ResponseBody>plati(@Path("kime")String kime,@Path("sifra")String sifra);
    
    @GET("app/podsistem1/zaustavi.podsistem1")
    Call<ResponseBody>zaustavi1();
    
    @GET("app/podsistem2/zaustavi.podsistem2")
    Call<ResponseBody>zaustavi2();
    
    @GET("app/podsistem3/zaustavi.podsistem3")
    Call<ResponseBody>zaustavi3();
    
    @GET("app/podsistem1/logovanje/{kime}/{sifra}")
    Call<ResponseBody> logovanje(@Path("kime")String kime,@Path("sifra")String sifra);
    
  }
  
  
  public static void placanje(MyAPI myAPI,String kime,String sifra){ 
       Call<ResponseBody> callGradovi=myAPI.plati(kime, sifra);

   
    callGradovi.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
            
            ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji.")|| odg.equals("Pogresna lozinka.") ){ 
                   korisnickoIme=null;
                   lozinka=null;
               }
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
           
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  public static void dohvGradove(MyAPI myAPI){ 
       Call<ResponseBody> callGradovi=myAPI.getGradovi();

   
    callGradovi.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
            
            ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji.")|| odg.equals("Pogresna lozinka.") ){ 
                   korisnickoIme=null;
                   lozinka=null;
               }
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
           
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  public static void dohvKorisnike(MyAPI myAPI,String kime){ 
      Call<ResponseBody> callKorisnici=myAPI.getKorisnici(kime);

   
    callKorisnici.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
             ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji.")|| odg.equals("Pogresna lozinka.") ){ 
                   korisnickoIme=null;
                   lozinka=null;
               }
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
   public static void napraviGrad(MyAPI myAPI,String naziv,String kime){ 
      Call<ResponseBody> callGrad=myAPI.createGrad(kime,naziv);
      
   
      callGrad.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji.")|| odg.equals("Pogresna lozinka.") ){ 
                   korisnickoIme=null;
                   lozinka=null;
               }
               Call<ResponseBody> callGrad1=myAPI.createGrad1(kime,naziv);
               callGrad1.enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                   ResponseBody body=response.body();
                   try{
                       String odg=body.string();
                       System.out.println(odg);
                       if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji.")|| odg.equals("Pogresna lozinka.") ){ 
                           korisnickoIme=null;
                           lozinka=null;
                       }
                   }
                   catch(Exception e){ 
                       System.out.println(" ");
                   }
                   sem.release();
                }

                @Override
                public void onFailure(Call<ResponseBody> call,Throwable t){
                    System.out.println(t.getMessage());
                    sem.release();
                }
            });
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
     
  }
  public static void kreirajKorisnika(MyAPI myAPI,String kime,String sifra,String ime,String prezime,String adresa,Double novac,String naziv){ 
      Call<ResponseBody> callKorisnik=myAPI.createKorisnik(kime, sifra, ime, prezime, adresa, novac, naziv);
      Call<ResponseBody>callKorisnik2=myAPI.createKorisnik2(kime,sifra,ime,prezime,novac);
   
    callKorisnik.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
            
            ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji.")|| odg.equals("Pogresna lozinka.") ){ 
                   korisnickoIme=null;
                   lozinka=null;
               }
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
    callKorisnik2.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
            ResponseBody body=response.body();
           try{
               System.out.println(body.string());
           }
           catch(Exception e){ 
               System.out.println(" ");
           }
           sem.release();
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void dodajNovac(MyAPI myAPI,String korIme,Double novac){ 
       Call<ResponseBody> callNovac1=myAPI.addMoney(korIme, novac);
       Call<ResponseBody> callNovac2=myAPI.addMoney2(korIme,novac);
       Call<ResponseBody> callNovac3=myAPI.addMoney3(korIme,novac);
   
    callNovac1.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
            ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
           
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
           
        }
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
    callNovac2.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
            ResponseBody body=response.body();
           try{
               System.out.println(body.string());
           }
           catch(Exception e){ 
             
           }
           sem.release();
        }
    
        
        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
     callNovac3.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
            ResponseBody body=response.body();
           try{
               System.out.println(body.string());
           }
           catch(Exception e){ 
             
           }
           sem.release();
        }
           @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void mijenjajAdresu(MyAPI myAPI,String adresa,String grad,String korIme){ 
    Call<ResponseBody> callAdresa=myAPI.changeAddress(adresa, grad, korIme);
    Call<ResponseBody> callAdresa1=myAPI.changeAddress1(adresa, grad, korIme);
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
    
    callAdresa1.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
           }
           catch(Exception e){ 
               
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void dodajKategoriju(MyAPI myAPI,String kime,String kat,String nadkat){ 
      Call<ResponseBody> callAdresa=myAPI.dodajKat(kime,kat, nadkat);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void dodajArtikall(MyAPI myAPI,String kime,String naziv,Double cijena,Double popust,String kat,String opis){ 
    Call<ResponseBody> callAdresa=myAPI.dodajArtikal(kime, naziv, cijena, popust, kat, opis);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void mijenjajC(MyAPI myAPI,String kime,int idPro,double cijena){ 
    Call<ResponseBody> callAdresa=myAPI.mijenjajCijenu(kime, idPro, cijena);
    Call<ResponseBody> callAdresa1=myAPI.mijenjajCijenu1(kime, idPro, cijena);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
     callAdresa1.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
              
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
   public static void mijenjajP(MyAPI myAPI,String kime,int idPro,double cijena){ 
    Call<ResponseBody> callAdresa=myAPI.mijenjajPopust(kime, idPro, cijena);
    Call<ResponseBody> callAdresa1=myAPI.mijenjajPopust1(kime, idPro, cijena);
    
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
    
    callAdresa1.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
   
  public static void dodajUKorpu(MyAPI myAPI,String kime,int idPro,int kolicina){ 
       Call<ResponseBody> callAdresa=myAPI.dodajKorpa(kime, idPro, kolicina);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
    public static void izbaciIzKorpe(MyAPI myAPI,String kime,int idPro,int kolicina){ 
       Call<ResponseBody> callAdresa=myAPI.izbaciKorpa(kime, idPro, kolicina);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
              
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
 }
    
  public static void dohvKategorije(MyAPI myAPI,String kime){ 
      Call<ResponseBody> callAdresa=myAPI.dohvKategorije(kime);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void dohvProdaju(MyAPI myAPI,String kime){ 
    Call<ResponseBody> callAdresa=myAPI.dohvProdaje(kime);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void dohvIzKorpe(MyAPI myAPI,String kime){ 
    Call<ResponseBody> callAdresa=myAPI.dohvKorpa(kime);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  public static void dohvNarudzbine(MyAPI myAPI,String kime){
       Call<ResponseBody> callAdresa=myAPI.getNarudzbina(kime);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
   public static void dohvTransakcije(MyAPI myAPI,String kime){
       Call<ResponseBody> callAdresa=myAPI.getTransakcije(kime);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
    public static void dohvNarudzbine2(MyAPI myAPI,String kime){
       Call<ResponseBody> callAdresa=myAPI.getNarudzbina2(kime);
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
              
           }
           catch(Exception e){ 
               System.out.println("Greska!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
    
  public static void Zaustavljanje(MyAPI myAPI){ 
           Call<ResponseBody> callAdresa=myAPI.zaustavi1();
           Call<ResponseBody>callAdresa2=myAPI.zaustavi2();
           Call<ResponseBody>callAdresa3=myAPI.zaustavi3();
   
    callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
    callAdresa2.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
    callAdresa3.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           try{
               String odg=body.string();
               System.out.println(odg);
               
           }
           catch(Exception e){ 
               System.out.println("Kraj rada!");
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  
  public static void Logovanje(MyAPI myAPI,String kime,String sifra){
      Call<ResponseBody> callAdresa=myAPI.logovanje(kime, sifra);
      
      
      callAdresa.enqueue(new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
           ResponseBody body=response.body();
           String odg="nista";
           try{
               odg=body.string();
               System.out.println(odg);
               if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji!") || odg.equals("Pogresna lozinka!")){ 
                   korisnickoIme=null;
                   lozinka=null;
               }
               
           }
           catch(Exception e){ 
               if(odg.equals("Korisnik sa zadatim korisnickim imenom ne postoji!") || odg.equals("Pogresna lozinka!")){ 
                   korisnickoIme=null;
                   lozinka=null;
               }
           }
           sem.release();
        }

        @Override
        public void onFailure(Call<ResponseBody> call,Throwable t){
            System.out.println(t.getMessage());
            sem.release();
        }
    });
  }
  public static void meni(){ 
      System.out.println("MENI:"+"\n"+
        "1. Kreiranje grada\n"+
        "2. Kreiranje korisnika\n"+
        "3. Dodavanje novca korisniku\n"+
        "4. Promena adrese i grada za korisnika\n"+
        "5. Kreiranje kategorije\n"+
        "6. Kreiranje artikla\n"+
        "7. Menjanje cene artikla\n"+
        "8. Postavljanje popusta za artikal\n"+
        "9. Dodavanje artikala u određenoj količini u korpu\n"+
        "10. Brisanje artikla u određenoj količini iz korpe\n"+
        "11. Plaćanje.\n"+
        "12. Dohvatanje svih gradova\n"+
        "13. Dohvatanje svih korisnika\n"+
        "14. Dohvatanje svih kategorija\n"+
        "15. Dohvatanje svih artikala koje prodaje korisnik koji je poslao zahtev\n"+
        "16. Dohvatanje sadržaja korpe korisnika koji je poslao zahtev\n"+
        "17. Dohvatanje svih narudžbina korisnika koji je poslao zahtev\n"+
        "18. Dohvatanje svih narudžbina\n"+
        "19. Dohvatanje svih transakcija\n"+
        "20.Ulogij se.\n"+
        "21.Izloguj se.\n"+
        "22.KRAJ RADA.\n");
   
  }
  
  
  public static void main(String[] args){
    Scanner stringScanner=new Scanner(System.in); 
    
    boolean logovanje=false;
    
    OkHttpClient ok=new OkHttpClient().newBuilder().readTimeout(120,TimeUnit.SECONDS).build();
    
   
    
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(API_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    
    MyAPI myAPI = retrofit.create(MyAPI.class);
    meni(); 
    boolean prosao=false;
    boolean adminRadnja=false;
    while(true){
        
        try{
        if(prosao && !logovanje && !adminRadnja)sem.acquire();
        if(logovanje)logovanje=false;
        if(adminRadnja)adminRadnja=false;
        System.out.println("Vas izbor: ");
        
            String br=stringScanner.nextLine();
            int i=Integer.parseInt(br);
            switch(i){ 
                case 1: 
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    if(!korisnickoIme.equals("admin")){ 
                        adminRadnja=true;
                        System.out.println("Ovu radnju moze obaviti samo administrator!");
                        break;
                    }
                    System.out.println("Unesite naziv grada: ");
                    String naziv=stringScanner.nextLine();
                    napraviGrad(myAPI,naziv,korisnickoIme);
                    break;
                case 2:
                    if(korisnickoIme!=null){ 
                        System.out.println("Da biste se registrovali,prvo se izlogujte.");
                        logovanje=true;
                        break;
                    }
                    System.out.println("Unesite korisnicko ime: ");
                    String kime=stringScanner.nextLine();
                    System.out.println("Unesite sifru: ");
                    String sifra=stringScanner.nextLine();
                    System.out.println("Unesite ime: ");
                    String ime=stringScanner.nextLine();
                    System.out.println("Unesite prezime: ");
                    String prezime=stringScanner.nextLine();
                    System.out.println("Unesite adresu: ");
                    String adresa=stringScanner.nextLine();
                    System.out.println("Unesite kolicinu novca(broj): ");
                    String n=stringScanner.nextLine();
                    Double novac=Double.parseDouble(n);
                    System.out.println("Unesite naziv grada: ");
                    String naziv2=stringScanner.nextLine();
                    
                    //treba da kreira korisnika u 2 baze
                    kreirajKorisnika(myAPI,kime,sifra,ime,prezime,adresa,novac,naziv2);
                    break;
                    
                case 3:
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
              
                    System.out.println("Unesite kolicinu novca(broj): ");
                    String n1=stringScanner.nextLine();
                    Double novac1=Double.parseDouble(n1);
                    
                    //ovo se treba azurirati u obje baze korisnika
                    dodajNovac(myAPI,korisnickoIme,novac1);
                    break;
                case 4: 
                     if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    System.out.println("Unesite adresu: ");
                    String adresa1=stringScanner.nextLine();
                    System.out.println("Unesite naziv grada: ");
                    String naziv3=stringScanner.nextLine();
                    
                    
                    
                    mijenjajAdresu(myAPI,adresa1,naziv3,korisnickoIme);
                    break;
                case 5: 
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    if(!korisnickoIme.equals("admin")){ 
                        adminRadnja=true;
                        System.out.println("Ovu radnju moze obaviti samo administrator!");
                        break;
                    }
                    System.out.println("Unesite naziv kategorije: ");
                    String kat=stringScanner.nextLine();
                    System.out.println("Ako (ne) zelite dodati nadkategoriju unesite ne/da: ");
                    String da=stringScanner.nextLine();
                    if(da.equals("da")){ 
                        System.out.println("Unesite naziv nadkategorije: ");
                        String nadkat=stringScanner.nextLine();
                        dodajKategoriju(myAPI,korisnickoIme,kat,nadkat);
                    }
                    else{ 
                        dodajKategoriju(myAPI,korisnickoIme,kat,null);
                    }
                    break;
                case 6:    
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    System.out.println("Unesite naziv artikla: ");
                    String nazivA=stringScanner.nextLine();
                    System.out.println("Unesite cijenu: ");
                    String c=stringScanner.nextLine();
                    Double cijena=Double.parseDouble(c);
                    System.out.println("Unesite popust: ");
                    String p=stringScanner.nextLine();
                    Double popust=Double.parseDouble(p);
                    System.out.println("Unesite kategoriju kojoj artikal prirada: ");
                    String katA=stringScanner.nextLine();
                    System.out.println("Ako hocete da unesete opis upisite da,inace ne: ");
                    String izb=stringScanner.nextLine();
                    if(izb.equals("da")){ 
                        System.out.println("Unesite opis: ");
                        String opis=stringScanner.nextLine();
                        dodajArtikall(myAPI,korisnickoIme,nazivA,cijena,popust,katA,opis);
                        
                    }
                    else{ 
                        dodajArtikall(myAPI,korisnickoIme,nazivA,cijena,popust,katA,null);
                        
                    }
                    break;
                case 7: 
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    System.out.println("Unesite identifikacioni broj proizvoda: ");
                    String brr=stringScanner.nextLine();
                    int idr=Integer.parseInt(brr);
                    System.out.println("Unesite cijenu proizvoda: ");
                    String ci=stringScanner.nextLine();
                    double cijenaA=Double.parseDouble(ci);
                    mijenjajC(myAPI,korisnickoIme,idr,cijenaA);
                    break;
                case 8: 
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    System.out.println("Unesite identifikacioni broj proizvoda: ");
                    String brrr=stringScanner.nextLine();
                    int idrr=Integer.parseInt(brrr);
                    System.out.println("Unesite popust proizvoda: ");
                    String pi=stringScanner.nextLine();
                    double popustA=Double.parseDouble(pi);
                    mijenjajP(myAPI,korisnickoIme,idrr,popustA);
                   
                    break;
                case 9: 
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    System.out.println("Unesite identifikacioni broj proizvoda: ");
                    String idd=stringScanner.nextLine();
                    int idP=Integer.parseInt(idd);
                    System.out.println("Unesite zelejnu kolicinu za dodavanje u korpu: ");
                    String koll=stringScanner.nextLine();
                    int kol=Integer.parseInt(koll);
                    
                    dodajUKorpu(myAPI,korisnickoIme,idP,kol);
                    break;
                case 10: 
                     if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    System.out.println("Unesite identifikacioni broj proizvoda: ");
                    String idd1=stringScanner.nextLine();
                    int idP1=Integer.parseInt(idd1);
                    System.out.println("Unesite zelejnu kolicinu za uklanjanje iz korpe korpu: ");
                    String koll1=stringScanner.nextLine();
                    int kol1=Integer.parseInt(koll1);
                    
                    izbaciIzKorpe(myAPI,korisnickoIme,idP1,kol1);
                    break;
                case 11:
                     if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                     placanje(myAPI,korisnickoIme,lozinka);
                     break;
                case 12:
                    //moze bez logovanja,za registraciju
                    dohvGradove(myAPI);
                    break;
                case 13: 
                     if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    if(!korisnickoIme.equals("admin")){ 
                        adminRadnja=true;
                        System.out.println("Ovu radnju moze obaviti samo administrator!");
                        break;
                    }
                    dohvKorisnike(myAPI,korisnickoIme);
                    break;
                case 14: 
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    dohvKategorije(myAPI,korisnickoIme);
                    break;
                case 15:
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    dohvProdaju(myAPI,korisnickoIme);
                    break;
                case 16:
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    dohvIzKorpe(myAPI,korisnickoIme);
                    break;
                case 17:
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    dohvNarudzbine(myAPI,korisnickoIme);
                    break;
                case 18: 
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    if(!korisnickoIme.equals("admin")){
                        adminRadnja=true;
                        System.out.println("Ovu radnju moze obaviti samo administrator!");
                        break;
                    }
                    dohvNarudzbine2(myAPI,korisnickoIme);
                    break;
                case 19:
                    if(korisnickoIme==null){ 
                        System.out.println("Niste ulogovani.");
                        logovanje=true;
                        break;
                    }
                    if(!korisnickoIme.equals("admin")){ 
                        adminRadnja=true;
                        System.out.println("Ovu radnju moze obaviti samo administrator!");
                        break;
                    }
                    dohvTransakcije(myAPI,korisnickoIme);
                    break;
                case 20:
                    System.out.println("Korisnicko ime: ");
                    korisnickoIme=stringScanner.nextLine();
                    System.out.println("Lozinka: ");
                    lozinka=stringScanner.nextLine();
                    Logovanje(myAPI,korisnickoIme,lozinka);
                    if(lozinka!=null && korisnickoIme!=null)logovanje=true;
                    break;
                case 21: 
                    korisnickoIme=null;
                    lozinka=null;
                    logovanje=true;
                    break;
                case 22:
                    Zaustavljanje(myAPI);
                    kraj=true;
                    break;
                
                default: 
                    System.out.println("Nekorektan izbor iz menija!Pokusajte ponovo.");
                    logovanje=true;
                    
              
                    
                    
            }
            if(kraj)break;
            if(!prosao && !logovanje)prosao=true;
            Thread.sleep(3000); 
            
            
            
        }
        catch (Exception ex) {
            Logger.getLogger(Klijent.class.getName()).log(Level.SEVERE, null, ex);
        }

       
               
    }
  }
}
