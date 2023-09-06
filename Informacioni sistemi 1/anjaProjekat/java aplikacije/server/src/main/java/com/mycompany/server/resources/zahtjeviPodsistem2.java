/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.server.resources;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import podsistem2.Zaustavi;
import podsistem2.Dohvatanje;
import podsistem2.KategorijaStvari;
import podsistem2.KorisnikStvari;
import podsistem2.Novac;
import podsistem2.ArtikalStvari;
import podsistem2.PopustCijena;
import podsistem2.dodajKorpa;

/**
 *
 * @author user2
 */
@Path("podsistem2")
public class zahtjeviPodsistem2 {
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue2")
    private Queue myQueue2;
    
    @Resource(lookup="topic1")
    private Topic myTopic;
    
    
    @POST
    @Path("kreiraj.korisnik/{kime}/{sifra}/{ime}/{prezime}/{novac}")
    public Response kreirajKorisnika(@PathParam("kime")String kime,@PathParam("sifra")String sifra,@PathParam("ime")String ime,@PathParam("prezime")String prezime,@PathParam("novac")Double novac){ 
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        if(kime==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Korisnicko ime nije unijeto!").build();
        if(sifra==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Sifra nije unijeta!").build();
        if(ime==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Ime nije unijeto!").build();
        if(prezime==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Prezime nije unijeto!").build();
        if(novac==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Novac nije unijet!").build();
        
        KorisnikStvari korStvari=new KorisnikStvari();
        korStvari.setkIme(kime);
        korStvari.setSifra(sifra);
        korStvari.setIme(ime);
        korStvari.setPrezime(prezime);
        korStvari.setNovac(novac);
        
        ObjectMessage om=context.createObjectMessage(korStvari);
        
        producer.send(myQueue2,om);
       

        return Response.noContent().build();
        
    }
    //dodavanje zadate kolicine novca zadatom korisniku
    @PUT
    @Path("dodaj.novac/{korIme}/{kolicina}")
    public Response dodajNovac(@PathParam("korIme")String korIme,@PathParam("kolicina")double n){
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Novac novac=new Novac();
        novac.setKorisnickoIme(korIme);
        
        novac.setNovac(n);
        
        ObjectMessage om=context.createObjectMessage(novac);

        producer.send(myQueue2,om);
        
        return Response.noContent().build();


    }
    
    //kreiranje kategorije
    @POST 
    @Path("dodaj.kategoriju/{kime}/{nazivKat}")
    public Response dodajKat(@PathParam("kime")String kime,@PathParam("nazivKat")String nazivKat,@QueryParam("nazivNadKat")String nazivNadKat){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        
        
        KategorijaStvari kats=new KategorijaStvari();
        kats.setNaziv(nazivKat);
        kats.setKime(kime);
        
        if(nazivNadKat==null) kats.setNazivNadKat(null);
        else kats.setNazivNadKat(nazivNadKat);
        
        ObjectMessage om=context.createObjectMessage(kats);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
    }
    
    //kreiranjeArtikla
    @POST
    @Path("kriraj.artikal/{kime}/{naziv}/{cijena}/{popust}/{kategorija}")
    public Response dodajArtikal(@PathParam("kime")String kime,@PathParam("naziv")String naziv,@PathParam("cijena")Double cijena,@PathParam("popust")Double popust,@PathParam("kategorija")String kat,@QueryParam("opis")String opis){ 
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        ArtikalStvari as=new ArtikalStvari();
        
        as.setNaziv(naziv);
        as.setCijena(cijena);
        as.setPopust(popust);
        as.setKategorija(kat);
        as.setKime(kime);
        
        
        if(opis!=null)as.setOpis(opis);
        else as.setOpis(null);
        
        ObjectMessage om=context.createObjectMessage(as);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
        
        
    }
    
    //promjena cijene i popusta
    @PUT 
    @Path("mijenjaj.cijenju/{kime}/{idPro}/{cijena}")
    public Response mijenjajCijenu(@PathParam("kime")String kime,@PathParam("idPro")int idPro,@PathParam("cijena")Double cijena){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        PopustCijena pop=new PopustCijena();
        
        pop.setIzbor(0);
        pop.setKime(kime);
        pop.setIdPro(idPro);
     
        pop.setVal(cijena);
        
        ObjectMessage om=context.createObjectMessage(pop);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
        
    }
     @PUT 
    @Path("mijenjaj.popust/{kime}/{idPro}/{popust}")
    public Response mijenjajPopust(@PathParam("kime")String kime,@PathParam("idPro")int idPro,@PathParam("popust")Double popust){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        PopustCijena pop=new PopustCijena();
        
        pop.setIzbor(1);
        pop.setKime(kime);
        pop.setIdPro(idPro);
        
        pop.setVal(popust);
        
        ObjectMessage om=context.createObjectMessage(pop);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
        
    }
    
    @PUT
    @Path("dodaj.korpa/{kime}/{idPro}/{kolicina}")
    public Response dodajKorpa(@PathParam("kime")String kime,@PathParam("idPro")int idPro,@PathParam("kolicina")int kolicina){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        dodajKorpa dodaj=new dodajKorpa();
        dodaj.setKime(kime);
        
        dodaj.setIdPro(idPro);
        dodaj.setKolicina(kolicina);
        dodaj.setIzbor(0);
        
        ObjectMessage om=context.createObjectMessage(dodaj);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
    }
    
    
    @DELETE
    @Path("izbaci.korpa/{kime}/{idPro}/{kolicina}")
    public Response izbaciKorpa(@PathParam("kime")String kime,@PathParam("idPro")int idPro,@PathParam("kolicina")int kolicina){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        
        
        dodajKorpa dodaj=new dodajKorpa();
        dodaj.setKime(kime);
    
        dodaj.setIdPro(idPro);
        dodaj.setKolicina(kolicina);
        dodaj.setIzbor(1);
        
        ObjectMessage om=context.createObjectMessage(dodaj);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
    }
    
    
    @GET
    @Path("dohvati.kategorije/{kime}")
    public Response dohvKategorije(@PathParam("kime")String kime){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
       
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setIzbor(0);
        dohv.setKime(kime);
        
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
    }
    
    @GET
    @Path("dohvati.prodaje/{kime}")
    public Response dohvProdaje(@PathParam("kime")String kime){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setIzbor(1);
        dohv.setKime(kime);
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
    }
    
    @GET
    @Path("dohvati.korpa/{kime}")
    public Response dohvKorpa(@PathParam("kime")String kime){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
    
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setIzbor(2);
        dohv.setKime(kime);
      
        
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue2,om);
        
        TextMessage tm=(TextMessage)consumer.receive();
        String text=null;
        try{ 
            text=tm.getText();
        }
        catch(JMSException ex){ 
            Logger.getLogger(zahtjeviPodsistem1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(Response.Status.OK).entity(text).build();
    }
    
     @GET
    @Path("zaustavi.podsistem2")
    public Response zaustavi(){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Zaustavi z=new Zaustavi();
        
        ObjectMessage om=context.createObjectMessage(z);
        producer.send(myQueue2,om);
         
         
        return Response.noContent().build();
    }
}
