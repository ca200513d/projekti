/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import entiteti.*;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author user2
 */
public class Main {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue1")
    private static Queue myQueue;
    
    @Resource(lookup="queue3")
    private static Queue queue3;
    
    
    @Resource(lookup="topic1")
    private static Topic myTopic;
    
    public static void main(String[] args) {
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("podsistem1PU");
        EntityManager em=emf.createEntityManager();
        
        
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue);
        
        try{
            
            while(true){ 
                
                
                ObjectMessage om=(ObjectMessage)consumer.receive(); 
                String povratnaPoruka=null;
                
                if(om.getObject() instanceof Log){ 
                    Log log=(Log)om.getObject();
                    povratnaPoruka=ulogujSe(log,em);
                }
                else if(om.getObject() instanceof SmanjiNovac){ 
                    SmanjiNovac sn=(SmanjiNovac)om.getObject();
                    Korisnik korisnik=em.find(Korisnik.class,sn.getIdKor());
                    if(korisnik!=null){ 
                        try{ 
                            if(sn.isSmanji()){ 
                                em.getTransaction().begin();
                                korisnik.setNovac(korisnik.getNovac()-sn.getKolicina());
                                em.getTransaction().commit();
                            }
                            else{
                                em.getTransaction().begin();
                                korisnik.setNovac(korisnik.getNovac()+sn.getKolicina());
                                em.getTransaction().commit();
                            }
                            
                        }
                        finally{ 
                            if(em.getTransaction().isActive())em.getTransaction().rollback();
                        }
                    }
                    continue;
                }
                if(om.getObject() instanceof VratiId){ 
                    VratiId idd=(VratiId)om.getObject();
                    String naziv=idd.getNaziv();
                    List<Grad>gradovi=em.createNamedQuery("Grad.findByNaziv",Grad.class).setParameter("naziv", naziv).getResultList();
                    int id=gradovi.get(0).getIdGra();
                    idd.setIdGra(id);
                    ObjectMessage om1=context.createObjectMessage(idd);
                    producer.send(queue3,om1);
                    continue;
                }
                if(om.getObject() instanceof Kupovina){ 
                   
                    Kupovina k =(Kupovina)om.getObject();
                    k=korisnikKupuje(k,em);
                    ObjectMessage om2=context.createObjectMessage(k);
                    producer.send(queue3,om2);
                    continue;
                }
                else if(om.getObject() instanceof GradStvari){
                    GradStvari gs=(GradStvari)om.getObject();
                    povratnaPoruka=kreirajGrad(gs,em);
                }
                else if(om.getObject() instanceof KorisnikStvari){ 
                    KorisnikStvari ks=(KorisnikStvari)om.getObject();
                    povratnaPoruka=kreirajKorisnika(ks,em);
                }

                else if(om.getObject() instanceof Novac){ 
                    Novac n=(Novac)om.getObject();
                    double novac=n.getNovac();
                    String kime=n.getKorisnickoIme();
                    
                    povratnaPoruka=dodajNovacKorisniku(kime,novac,em);
                }
                
                else if(om.getObject() instanceof Dohvatanje){
                    
                    Dohvatanje dohvatanje=(Dohvatanje)om.getObject();
                    switch(dohvatanje.getDohv()){
                        case 1:
                            povratnaPoruka=dohvKorisnike(dohvatanje,em);
                            break;
                        case 2:
                            povratnaPoruka=dohvGradove(em);
                            break;
                    }
                    
                }
                else if(om.getObject() instanceof PromjenaAdrese){
                    
                    PromjenaAdrese promjenaAdrese=(PromjenaAdrese)om.getObject();
                    povratnaPoruka=promjenaAdrese(promjenaAdrese,em);
                }
                
                else if(om.getObject() instanceof Zaustavi){ 
                    break;
                }
                
                
                
                
              
                TextMessage tm=context.createTextMessage(povratnaPoruka);
                producer.send(myTopic, tm);


            }
            
        }
        catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.close();
        emf.close();

        Message mes;
        do{ 
            mes=consumer.receiveNoWait();
        }
        while(mes!=null);
        
        
        
    }
    
    
    //ZAHTJEVI 
    public static String kreirajGrad(GradStvari gradStvari,EntityManager em){
       try{ 
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", gradStvari.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
        
        Korisnik korisnik=korisnici.get(0);
        
        
        List<Grad>gradovi= em.createNamedQuery("Grad.findByNaziv",Grad.class).setParameter("naziv",gradStvari.getNaziv()).getResultList();
    
        
        if(gradovi!=null && !gradovi.isEmpty())return "Grad sa zadatim nazivom vec postoji.";
        
        Grad grad=new Grad();
        grad.setNaziv(gradStvari.getNaziv());
        
        em.getTransaction().begin();
        
        em.persist(grad);
        em.flush();
        
        em.getTransaction().commit();
       }
       
        finally{ 
            if(em.getTransaction().isActive())em.getTransaction().rollback();
            
        }
        return "Kreiran grad: "+gradStvari.getNaziv()+".";
       
      
    }
    
    public static String kreirajKorisnika(KorisnikStvari korStvari,EntityManager em){ 
        
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme",korStvari.getkIme()).getResultList();
        
        if(korisnici!=null && !korisnici.isEmpty())return "Korisnicko ime vec postoji.";
        
        
        List<Grad>gradovi=em.createNamedQuery("Grad.findByNaziv",Grad.class).setParameter("naziv",korStvari.getIdGra()).getResultList();
        if(gradovi==null || gradovi.isEmpty())return "Ne postoji grad sa zadatim imenom.";
        Grad grad=gradovi.get(0);
        
        Korisnik korisnik=new Korisnik();
        korisnik.setIdGra(grad);
        korisnik.setAdresa(korStvari.getAdresa());
        korisnik.setIme(korStvari.getIme());
        korisnik.setPrezime(korStvari.getPrezime());
        korisnik.setKIme(korStvari.getkIme());
        korisnik.setSifra(korStvari.getSifra());
        korisnik.setNovac(korStvari.getNovac());
        
        try{ 
            em.getTransaction().begin();
            
            em.persist(korisnik);
            em.flush();
            
            em.getTransaction().commit();
            
        }
        finally{ 
            if(em.getTransaction().isActive())em.getTransaction().rollback();
            
        }
        return "Kreiran korisnik: "+korStvari.getkIme()+ ".";
        
    }
    
    public static String dodajNovacKorisniku(String korIme,double novac,EntityManager em){ 
        
        
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", korIme).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
        
        Korisnik korisnik=korisnici.get(0);
        
        
        
        
       try{
            em.getTransaction().begin();
            korisnik.setNovac(korisnik.getNovac()+novac);
            em.getTransaction().commit();
       }
       finally{ 
            if(em.getTransaction().isActive())em.getTransaction().rollback();
       }
       
        return "Novac korisnika je uvecan.";
    }
    
    public static String dohvKorisnike(Dohvatanje d,EntityManager em){
        
         List<Korisnik>korisnicii=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", d.getKime()).getResultList();
        if(korisnicii==null || korisnicii.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
        
        Korisnik korisnikk=korisnicii.get(0);
        
        
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findAll").getResultList();
        
        if(korisnici==null || korisnici.isEmpty())return "Ne postoji nijedan korisnik.";
        
        StringBuffer sb=new StringBuffer();
        
        for(Korisnik korisnik:korisnici){
            sb.append("KORISNIK BROJ "+korisnik.getIdKor()+"\n");
            sb.append("Korisnicko ime: "+korisnik.getKIme()+"\n");
            sb.append("Sifra: "+korisnik.getSifra()+"\n");
            sb.append("Ime: "+korisnik.getIme()+"\n");
            sb.append("Prezime: "+korisnik.getPrezime()+"\n");
            sb.append("Adresa: "+korisnik.getAdresa()+"\n");
            sb.append("Kolicna novca: "+korisnik.getNovac()+"\n");
            Grad grad=korisnik.getIdGra();
            sb.append("Grad: "+grad.getNaziv()+"\n");
            sb.append("\n");
            
        }
        
        return sb.toString();
    }
    
    public static String dohvGradove(EntityManager em){
        
        List<Grad>gradovi=em.createNamedQuery("Grad.findAll").getResultList();
        
        if(gradovi==null || gradovi.isEmpty())return "Ne postoji ni jedan grad.";
        
        StringBuffer sb=new StringBuffer();
        
        for(Grad grad:gradovi){
            sb.append("GRAD BROJ "+grad.getIdGra()+"\n");
            sb.append("Naziv grada: "+grad.getNaziv()+"\n");
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
    public static String promjenaAdrese(PromjenaAdrese pa,EntityManager em){
       
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", pa.getKorisnickoIme()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
        
        Korisnik korisnik=korisnici.get(0);
        
        
        List<Grad>gradovi=em.createNamedQuery("Grad.findByNaziv",Grad.class).setParameter("naziv",pa.getGrad()).getResultList();
        
        if(gradovi==null || gradovi.isEmpty())return "Grad sa zadatim nazivom ne postoji.";
        
        Grad grad=gradovi.get(0);
        
        try{
            em.getTransaction().begin();
            korisnik.setAdresa(pa.getAdresa());
            korisnik.setIdGra(grad);
            em.getTransaction().commit();
        }
        finally{
            if(em.getTransaction().isActive())em.getTransaction().rollback();
            
        }
        
        
        
        return "Promjenjena je adresa korisniku "+pa.getKorisnickoIme()+".";
    }
    
    public static Kupovina korisnikKupuje(Kupovina k,EntityManager em){ 
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", k.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return null;
        
        Korisnik korisnik=korisnici.get(0);
        
        if(!k.getSifra().equals(korisnik.getSifra()))return null;
        
        k.setIme(korisnik.getIme());
        k.setPrezime(korisnik.getPrezime());
        k.setAdresa(korisnik.getAdresa());
        k.setIdGra(korisnik.getIdGra().getIdGra());
        k.setIdKor(korisnik.getIdKor());
        k.setNovac(korisnik.getNovac());
        
        return k;
        
        
        
    }
    
    public static String ulogujSe(Log log,EntityManager em){ 
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", log.getKime()).getResultList();
        
     
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji!";
        
        Korisnik korisnik=korisnici.get(0);
        if(!korisnik.getSifra().equals(log.getSifra()))return "Pogresna lozinka!";
        
        
        
        
        return "Uspjesno ste se ulogovali!";
    }
}
