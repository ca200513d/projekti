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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import podsistem3.Novac;
import podsistem3.PromjenaAdrese;
import podsistem3.GradStvari;
import podsistem3.Zaustavi;
import podsistem3.Dohvatanje;
import podsistem3.Placanje;
import podsistem3.PopustCijena;

/**
 *
 * @author user2
 */
@Path("podsistem3")
public class zahtjeviPodsistem3 {
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    
    @Resource(lookup="queue3")
    private Queue myQueue3;
    
    @Resource(lookup="topic1")
    private Topic myTopic;
    
    @POST
    @Path("kreiraj.grad/{kime}/{Naziv}")
    public Response napraviGrad(@PathParam("kime")String kime,@PathParam("Naziv")String naziv){ 
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
         if(!kime.equals("admin"))return Response.noContent().build();
        if(naziv!=null){ 
            GradStvari gs=new GradStvari();
            gs.setNaziv(naziv);
            gs.setKime(kime);
           

            ObjectMessage om=context.createObjectMessage(gs);

            producer.send(myQueue3, om);

        }  
        return Response.noContent().build();
    }

    @GET
    @Path("plati/{kime}/{sifra}")
    public Response plati(@PathParam("kime")String kime, @PathParam("sifra")String sifra){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        
        Placanje p=new Placanje();
        p.setKime(kime);
        p.setSifra(sifra);
        
        ObjectMessage om=context.createObjectMessage(p);

        producer.send(myQueue3,om);
        
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
    @Path("dohvati.narudzbine/{kime}")
    public Response getNarudzbina(@PathParam("kime")String kime){ 
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setIzbor(0);
        dohv.setKime(kime);
        
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue3,om);
        
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
    @Path("dohvati.narudzbine2/{kime}")
    public Response getNarudzbina2(@PathParam("kime")String kime){ 
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setIzbor(1);
        dohv.setKime(kime);
      
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue3,om);
        
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
    @Path("dohvati.transakcije/{kime}")
    public Response getTransakcije(@PathParam("kime")String kime){ 
         JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setIzbor(2);
        dohv.setKime(kime);
       
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue3,om);
        
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
    @Path("zaustavi.podsistem3")
    public Response zaustavi(){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Zaustavi z=new Zaustavi();
        
        ObjectMessage om=context.createObjectMessage(z);
        producer.send(myQueue3,om);
         
         
        return Response.noContent().build();
    }
    
    
    @PUT
    @Path("promjeni.adresu/{Adresa}/{NazivGrada}/{KorIme}")
    public Response promjeniAdresu(@PathParam("Adresa")String adresa,@PathParam("NazivGrada")String naziv,@PathParam("KorIme")String korIme){
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        PromjenaAdrese pa=new PromjenaAdrese();
        
        pa.setAdresa(adresa);
        pa.setGrad(naziv);
        pa.setKorisnickoIme(korIme);
        
        
        ObjectMessage om=context.createObjectMessage(pa);

        producer.send(myQueue3,om);
        
       return Response.noContent().build();
        
        
    }
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

        producer.send(myQueue3,om);


       return Response.noContent().build();


    }
    
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

        producer.send(myQueue3,om);
        
        return Response.noContent().build();
        
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

        producer.send(myQueue3,om);
        
        

        return Response.noContent().build();
        
    }
    

}
