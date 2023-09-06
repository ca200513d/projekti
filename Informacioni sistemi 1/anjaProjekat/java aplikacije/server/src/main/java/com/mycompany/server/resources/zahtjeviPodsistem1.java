package com.mycompany.server.resources;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import podsistem1.Dohvatanje;
import podsistem1.GradStvari;
import podsistem1.KorisnikStvari;
import podsistem1.Log;
import podsistem1.Novac;
import podsistem1.PromjenaAdrese;
import podsistem1.Zaustavi;

/**
 *
 * @author user2
 */

@Path("podsistem1")
public class zahtjeviPodsistem1 {
    
    
    @Resource(lookup="jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue1")
    private Queue myQueue1;
    
    @Resource(lookup="topic1")
    private Topic myTopic;
    
    //kreiranje novog grada sa zadatim nazivom
    @POST
    @Path("kreiraj.grad/{kime}/{Naziv}")
    public Response napraviGrad(@PathParam("kime")String kime,@PathParam("Naziv")String naziv){ 
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
         
        if(naziv!=null){ 
            GradStvari gs=new GradStvari();
            gs.setNaziv(naziv);
            gs.setKime(kime);
           

            ObjectMessage om=context.createObjectMessage(gs);

            producer.send(myQueue1, om);

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
        return Response.ok(Response.Status.BAD_REQUEST).entity("Naziv je null!").build();
    }
    
    //kreiranje korisnika sa zadatim parametrima putem forme
    @POST
    @Path("kreiraj.korisnik/{kime}/{sifra}/{ime}/{prezime}/{adresa}/{novac}/{grad}")
    public Response kreirajKorisnika(@PathParam("kime")String kime,@PathParam("sifra")String sifra,@PathParam("ime")String ime,@PathParam("prezime")String prezime,@PathParam("adresa")String adresa,@PathParam("novac")Double novac,@PathParam("grad")String grad){ 
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        if(kime==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Korisnicko ime nije unijeto!").build();
        if(sifra==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Sifra nije unijeta!").build();
        if(ime==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Ime nije unijeto!").build();
        if(prezime==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Prezime nije unijeto!").build();
        if(adresa==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Adresa nije unijeta!").build();
        if(novac==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Novac nije unijet!").build();
        if(grad==null)return Response.ok(Response.Status.BAD_REQUEST).entity("Grad nije unijet!").build();
        
        KorisnikStvari korStvari=new KorisnikStvari();
        korStvari.setkIme(kime);
        korStvari.setSifra(sifra);
        korStvari.setIme(ime);
        korStvari.setPrezime(prezime);
        korStvari.setAdresa(adresa);
        korStvari.setIdGra(grad);
        korStvari.setNovac(novac);
        
        ObjectMessage om=context.createObjectMessage(korStvari);
        
        producer.send(myQueue1,om);
        
        
        
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

        producer.send(myQueue1,om);


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
    
    //dohvatanje svih korisnika
    @GET
    @Path("svi.korisnici/{kime}")
    public Response dohvKorisnike(@PathParam("kime")String kime){
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setDohv(1);
        dohv.setKime(kime);
       
        
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue1,om);
        
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
    
    //dohvatanje svih gradova
    @GET
    @Path("svi.gradovi")
    public Response dohvGradove(){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Dohvatanje dohv=new Dohvatanje();
        dohv.setDohv(2);
        
        
        ObjectMessage om=context.createObjectMessage(dohv);

        producer.send(myQueue1,om);
        
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

        producer.send(myQueue1,om);
        
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
    @Path("zaustavi.podsistem1")
    public Response zaustavi(){ 
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Zaustavi z=new Zaustavi();
        
        ObjectMessage om=context.createObjectMessage(z);
        producer.send(myQueue1,om);
         
         
        return Response.noContent().build();
    }
    
    @GET
    @Path("logovanje/{kime}/{sifra}")
    public Response logovanje(@PathParam("kime")String kime,@PathParam("sifra")String sifra){
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myTopic);
        
        Log log=new Log();
        log.setKime(kime);
        log.setSifra(sifra);
        
        ObjectMessage om=context.createObjectMessage(log);

        producer.send(myQueue1,om);
        
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
}
