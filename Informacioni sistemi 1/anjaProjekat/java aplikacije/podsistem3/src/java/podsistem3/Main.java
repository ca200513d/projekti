/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem3;

import entiteti.*;
import java.util.Date;
import java.util.List;
import javax.jms.Queue;
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
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import podsistem2.IdKor;
import podsistem1.Kupovina;
import podsistem1.SmanjiNovac;
import podsistem1.VratiId;
import podsistem2.DohvProizvod;
import podsistem2.Elem;

/**
 *
 * @author user2
 */
public class Main {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup="queue3")
    private static Queue myQueue;
    
    @Resource(lookup="queue1")
    private static Queue myQueue1;
    
    @Resource(lookup="queue2")
    private static Queue myQueue2;
    
    @Resource(lookup="topic1")
    private static Topic myTopic;
    
    public static void main(String[] args) {
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("podsistem3PU");
        EntityManager em=emf.createEntityManager();
        
        
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue);
        
        try{
            while(true){ 
                
                ObjectMessage om=(ObjectMessage)consumer.receive(); 
                String povratnaPoruka=null;
                
                if(om.getObject() instanceof Dohvatanje){ 
                    Dohvatanje dohv=(Dohvatanje)om.getObject();
                    int izbor=dohv.getIzbor();
                    
                    switch(izbor){ 
                        case 0:
                            povratnaPoruka=dohvNarudzbine1(dohv,em);
                            break;
                        case 1: 
                            povratnaPoruka=dohvNarudzbine2(dohv,em);
                            break;
                        case 2: 
                            povratnaPoruka=dohvTransakcije(dohv,em);
                            break;
                    }
                }
                else if(om.getObject() instanceof GradStvari){ 
                    GradStvari grad=(GradStvari)om.getObject();
                    kreirajGrad(grad,em,producer,consumer,context);
                }
                else if(om.getObject() instanceof Placanje){ 
                    Placanje p=(Placanje)om.getObject();
                    povratnaPoruka=kupovina(p,em,context,producer,consumer);
                }
                else if(om.getObject() instanceof PopustCijena){
                    PopustCijena pop=(PopustCijena)om.getObject();
                    int izbor=pop.getIzbor();
                    switch(izbor){ 
                        case 0: mijenjajCijenu(pop,em,context,consumer,producer);
                                break;
                        case 1: mijenjajPopust(pop,em,context,consumer,producer);
                                break;
                    }
                }
                else if(om.getObject() instanceof Novac){ 
                    Novac n=(Novac)om.getObject();
                    dodajNovacKorisniku(n.getKorisnickoIme(),n.getNovac(),em);
                    
                }
                else if(om.getObject() instanceof PromjenaAdrese){ 
                    PromjenaAdrese pa=(PromjenaAdrese)om.getObject();
                    promjenaAdrese(pa,em);
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
    
    
    public static String dohvNarudzbine1(Dohvatanje dohv ,EntityManager em){ 
         List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", dohv.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik nije nista prodao ili kupio.";
        
        Korisnik korisnik=korisnici.get(0);
        
        
        
        
        List<Narudzbina>narudzbine=em.createQuery("SELECT N FROM Narudzbina N WHERE N.idKor.idKor=:id",Narudzbina.class).setParameter("id",korisnik.getIdKor()).getResultList();
        
        if(narudzbine==null || narudzbine.isEmpty())return "Ne postoje narudzbine za korisnika!";
        
        StringBuffer sb=new StringBuffer();
        
        for(Narudzbina nar:narudzbine){ 
            sb.append("NARUDZBINA BR."+nar.getIdNar()+":\n");
            
            
            List<Stavka>stavke=em.createNamedQuery("Stavka.findAll",Stavka.class).getResultList();
            if(stavke==null || stavke.isEmpty())continue;
            for(Stavka s:stavke){ 
                if(s.getIdNar().equals(nar)){
                    sb.append("STAVKA BR. "+s.getIdSta()+"\n");
                    sb.append("Ime proizvoda:"+s.getIdPro().getNaziv()+"\n");
                    sb.append("Cijena jedog prozivoda(sa popustom) :"+s.getJedinicnaCijena()+"\n");
                    sb.append("Kolicina: "+s.getKolicina()+"\n");
                }
                
            }
            sb.append("Ukupna cijena:"+ nar.getUkupnaCijena()+"\n");
            sb.append("Adresa: "+nar.getAdresa()+"\n");
            sb.append("Grad: "+nar.getIdGra().getNaziv()+"\n");
            sb.append("Vrijeme kreiranja: "+nar.getVrijeme()+"\n\n");
            
        }
        
        return sb.toString();
    }
     public static String dohvNarudzbine2(Dohvatanje dohv ,EntityManager em){ 
         List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", dohv.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
        
        Korisnik korisnik=korisnici.get(0);
        
        
        
        
        List<Narudzbina>narudzbine=em.createNamedQuery("Narudzbina.findAll",Narudzbina.class).getResultList();
        
        if(narudzbine==null || narudzbine.isEmpty())return "Ne postoje narudzbine!";
        
        StringBuffer sb=new StringBuffer();
        
      for(Narudzbina nar:narudzbine){ 
            sb.append("NARUDZBINA BR."+nar.getIdNar()+":\n");
            
            
            List<Stavka>stavke=em.createNamedQuery("Stavka.findAll",Stavka.class).getResultList();
            if(stavke==null || stavke.isEmpty())continue;
            for(Stavka s:stavke){ 
                if(s.getIdNar().equals(nar)){
                    sb.append("STAVKA BR. "+s.getIdSta()+"\n");
                    sb.append("Ime proizvoda:"+s.getIdPro().getNaziv()+"\n");
                    sb.append("Cijena jedog prozivoda(sa popustom) :"+s.getJedinicnaCijena()+"\n");
                    sb.append("Kolicina: "+s.getKolicina()+"\n");
                }
                
            }
            sb.append("Ukupna cijena:"+ nar.getUkupnaCijena()+"\n");
            sb.append("Adresa: "+nar.getAdresa()+"\n");
            sb.append("Grad: "+nar.getIdGra().getNaziv()+"\n");
            sb.append("Vrijeme kreiranja: "+nar.getVrijeme()+"\n\n");
            
        }
        return sb.toString();
    }
   
    public static String dohvTransakcije(Dohvatanje dohv,EntityManager em){ 
        
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", dohv.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
        
        Korisnik korisnik=korisnici.get(0);
        
        
        
        
        List<Transakcija>transakcije=em.createNamedQuery("Transakcija.findAll",Transakcija.class).getResultList();
        
        if(transakcije==null || transakcije.isEmpty())return "Ne postoje narudzbine!";
        
        StringBuffer sb=new StringBuffer();
        
        for(Transakcija nar:transakcije){ 
            sb.append("Transakcija BR."+nar.getIdNar()+":\n");
            sb.append("Ukupna suma:"+ nar.getSuma()+"\n");
            sb.append("Vrijeme kreiranja: "+nar.getVrijeme()+"\n");
            
        }
        
        return sb.toString();
     }
   public static String kupovina(Placanje p,EntityManager em,JMSContext context,JMSProducer producer,JMSConsumer consumer) throws JMSException{ 
          
            //dohvatanje osnovnih informacija iz podsistema1 o korisniku koji kupuje
            Kupovina k=new Kupovina();
            k.setKime(p.getKime());
            k.setSifra(p.getSifra());
            ObjectMessage om1=context.createObjectMessage(k);
            producer.send(myQueue1,om1);
            ObjectMessage om2=(ObjectMessage)consumer.receive();
            Kupovina kk=(Kupovina)om2.getObject();
            
            if(kk==null)return "Korisnicko ime ili sifra su pogresni.";
            
            //provjera da li je u korpi vise stvari nego sto ima novca
            IdKor korisnik=new IdKor();
            korisnik.setKime(k.getKime());
            ObjectMessage om3=context.createObjectMessage(korisnik);
            producer.send(myQueue2,om3);
            ObjectMessage om4=(ObjectMessage)consumer.receive();
            
            korisnik=(IdKor)om4.getObject();
            if(korisnik.isPraznaKorpa())return "Nista niste stavili u korpu!";
            if(!korisnik.isMozePlatiti() && !korisnik.isPraznaKorpa())return "Nemate dovoljno novca za ovu radnju!";
            
            //umanjivanje novca u sva 3 podistema
            kk.setNovac(kk.getNovac()-korisnik.getUkupnaCijena());
            SmanjiNovac sn=new SmanjiNovac();
            sn.setIdKor(kk.getIdKor());
            sn.setKolicina(korisnik.getUkupnaCijena());
            sn.setSmanji(true);
            ObjectMessage om5=context.createObjectMessage(sn);
            producer.send(myQueue1,om5);
            
            Korisnik k1=em.find(Korisnik.class,kk.getIdKor());
            if(k1!=null){
                try{ 
                    
                    em.getTransaction().begin();
                    k1.setNovac(k1.getNovac()-korisnik.getUkupnaCijena());
                    em.getTransaction().commit();
                }
                finally{ 
                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                }
            }
            //dodavanje novog korisnika,koji kupuje u bazu Korisnik
           
            else if(k1==null){ 
                k1=new Korisnik();
                k1.setIdKor(kk.getIdKor());
                k1.setIme(kk.getIme());
                k1.setPrezime(kk.getPrezime());
                k1.setKime(k.getKime());
                k1.setSifra(k.getSifra());
                k1.setAdresa(kk.getAdresa());
                k1.setNovac(kk.getNovac());
                Grad grad=em.find(Grad.class,kk.getIdGra());
                k1.setIdGra(grad);
                
                try{ 
                    
                    em.getTransaction().begin();
                    em.persist(k1);
                    em.flush();
                    em.getTransaction().commit();

                    }
                finally{ 
                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                }
            }
            
            //kreiranje narudzbine i transakcije
            
            StringBuffer sb=new StringBuffer();
            
            Narudzbina n=new Narudzbina();
            n.setIdGra(k1.getIdGra());
            n.setAdresa(k1.getAdresa());
            n.setIdKor(k1);
            n.setUkupnaCijena(korisnik.getUkupnaCijena());
            Date time=new Date(System.currentTimeMillis());
            n.setVrijeme(time);
            
            try{ 
                em.getTransaction().begin();
                em.persist(n);
                em.flush();
                em.getTransaction().commit();
            }
            finally{ 
                if(em.getTransaction().isActive())em.getTransaction().rollback();
            }
            
            
            Transakcija t=new Transakcija();
            t.setNarudzbina(n);
            t.setIdNar(n.getIdNar());
            t.setSuma(n.getUkupnaCijena());
            time=new Date(System.currentTimeMillis());
            t.setVrijeme(time);
            
            
            try{ 
                em.getTransaction().begin();
                em.persist(t);
                em.flush();
                em.getTransaction().commit();
            }
            finally{ 
                if(em.getTransaction().isActive())em.getTransaction().rollback();
            }
            
            sb.append("--------------------------RACUN-----------------------------\n");
            sb.append("Datum i vrijeme: "+t.getVrijeme().toString()+"\n");
            sb.append("Adresa za dostavu: "+n.getAdresa()+", "+n.getIdGra().getNaziv()+"\n");
            sb.append("NARUCENI PROIZVODI: \n");
            //dodati svaki proizvod,ako ne postoji na listu prodatih i kreiranje stavki za narudzbinu
            
            DohvProizvod d=new DohvProizvod();
            d.setKime(kk.getKime());
            
            ObjectMessage poruka=context.createObjectMessage(d);
            producer.send(myQueue2,poruka);
            
            ObjectMessage por=(ObjectMessage)consumer.receive();
            d=(DohvProizvod)por.getObject();
            
            List<Elem> dohv=d.getProizvodi();
            
            for(Elem elem:dohv){ 
                
                Proizvod pro=em.find(Proizvod.class,elem.getIdPro());
                
                if(pro==null){ 
                    pro=new Proizvod();
                    pro.setIdPro(elem.getIdPro());
                    pro.setNaziv(elem.getNaziv());
                    pro.setCijena(elem.getCijena());
                    pro.setPopust(elem.getPopust());
                    
                    try{ 
                        em.getTransaction().begin();
                        em.persist(pro);
                        em.flush();
                        em.getTransaction().commit();
                    }
                    finally{ 
                        if(em.getTransaction().isActive())em.getTransaction().rollback();
                    }
                }
                Korisnik vlasnik=em.find(Korisnik.class,elem.getKoProdaje());
                
                if(vlasnik==null){ 
                    vlasnik=new Korisnik();
                    vlasnik.setKime(elem.getKs().getkIme());
                    vlasnik.setSifra(elem.getKs().getSifra());
                    
                    vlasnik.setIme(elem.getKs().getIme());
                    vlasnik.setPrezime(elem.getKs().getPrezime());
                    
                    //jer u podsistemu2 nema adrese i grada
                    Kupovina k2=new Kupovina();
                    k2.setKime(elem.getKs().getkIme());
                    k2.setSifra(elem.getKs().getSifra());
                    ObjectMessage om7=context.createObjectMessage(k2);
                    producer.send(myQueue1,om7);
                    ObjectMessage om8=(ObjectMessage)consumer.receive();
                    k2=(Kupovina)om8.getObject();
                   
                    vlasnik.setAdresa(k2.getAdresa());
                    Grad grad=em.createNamedQuery("Grad.findByIdGra",Grad.class).setParameter("idGra",k2.getIdGra()).getSingleResult();
                    vlasnik.setIdGra(grad);
                    double cijena=elem.getCijena()-(elem.getCijena()*(elem.getPopust()/100));
                    vlasnik.setNovac(elem.getKs().getNovac()+elem.getKolicina()*cijena);
                    vlasnik.setIdKor(k2.getIdKor());
                    //povecavanje novca vlasnika u 1.podsistemu
                    SmanjiNovac snn=new SmanjiNovac();
                    snn.setKolicina(cijena*elem.getKolicina());
                    snn.setIdKor(elem.getKoProdaje());
                    snn.setSmanji(false);
                    ObjectMessage om6=context.createObjectMessage(snn);
                    producer.send(myQueue1,om6);
                    
                    try{ 
                    
                        em.getTransaction().begin();
                        em.persist(vlasnik);
                        em.flush();
                        em.getTransaction().commit();

                    }
                    finally{ 
                        if(em.getTransaction().isActive())em.getTransaction().rollback();
                    }
                    
                }
                else{ 
                    double cijena=elem.getCijena()-(elem.getCijena()*(elem.getPopust()/100));
                    //povecavanje novca vlasnika u 1.podsistemu
                    SmanjiNovac snn=new SmanjiNovac();
                    snn.setKolicina(cijena*elem.getKolicina());
                    snn.setIdKor(elem.getKoProdaje());
                    snn.setSmanji(false);
                    ObjectMessage om6=context.createObjectMessage(snn);
                    producer.send(myQueue1,om6);
                     try{ 
                    
                        em.getTransaction().begin();
                        
                        vlasnik.setNovac(vlasnik.getNovac()+cijena*elem.getKolicina());
                        em.getTransaction().commit();

                    }
                    finally{ 
                        if(em.getTransaction().isActive())em.getTransaction().rollback();
                    }
                }
                
                Stavka stavka=new Stavka();
                stavka.setIdPro(pro);
                double cijena=elem.getCijena()-(elem.getCijena()*(elem.getPopust()/100));
                stavka.setJedinicnaCijena(cijena);
                stavka.setKolicina(elem.getKolicina());
                stavka.setIdNar(n);
                
                 try{ 
                    
                    em.getTransaction().begin();
                    em.persist(stavka);
                    em.flush();
                    em.getTransaction().commit();

                }
                finally{ 
                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                }
                 sb.append("Prodavac: "+vlasnik.getKime()+"\n");
                 sb.append("Naziv: "+stavka.getIdPro().getNaziv()+"\n");
                 sb.append("Kolicina: "+stavka.getKolicina()+"\n");
                 sb.append("Cijena(sa uracunatim popustima): "+stavka.getJedinicnaCijena()+"\n");
                 sb.append("\n");
            }
            
            
            sb.append("UKUPNA CIJENA: "+korisnik.getUkupnaCijena()+"\n");
            return sb.toString();
   }
   
    
     public static String kreirajGrad(GradStvari gradStvari,EntityManager em,JMSProducer producer,JMSConsumer consumer1,JMSContext context) throws JMSException{
       try{ 
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", gradStvari.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
        
        Korisnik korisnik=korisnici.get(0);
        
       
        
        List<Grad>gradovi= em.createNamedQuery("Grad.findByNaziv",Grad.class).setParameter("naziv",gradStvari.getNaziv()).getResultList();
    
        
        if(gradovi!=null && !gradovi.isEmpty())return "Grad sa zadatim nazivom vec postoji.";
        
        Grad grad=new Grad();
        VratiId vratiId=new VratiId();
        vratiId.setNaziv(gradStvari.getNaziv());
        ObjectMessage om1=context.createObjectMessage(vratiId);
        producer.send(myQueue1,om1);
        ObjectMessage om2=(ObjectMessage)consumer1.receive();
        VratiId idd=(VratiId)om2.getObject();
        grad.setIdGra(idd.getIdGra());
        grad.setNaziv(gradStvari.getNaziv());
        
        em.getTransaction().begin();
        
        em.persist(grad);
        em.flush();
        
        em.getTransaction().commit();
       }
       
        finally{ 
            if(em.getTransaction().isActive())em.getTransaction().rollback();
            
        }
        return "Kreiran grad.";
       
      
    }
     
     
     
      public static void mijenjajCijenu(PopustCijena pc,EntityManager em,JMSContext context,JMSConsumer consumer,JMSProducer producer) throws JMSException{ 
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", pc.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return;
        
        Korisnik korisnik=korisnici.get(0);
        
        
        
        podsistem2.PopustCijena pcc=new podsistem2.PopustCijena();
        pcc.setIzbor(3);
        pcc.setKime(pc.getKime());
        
        pcc.setIdPro(pc.getIdPro());
        ObjectMessage om1=context.createObjectMessage(pcc);
        producer.send(myQueue2,om1);
        ObjectMessage om2=(ObjectMessage)consumer.receive();
        pcc=(podsistem2.PopustCijena)om2.getObject();
        if(pcc.getVal()==-1)return;
       
        Proizvod proizvod=em.find(Proizvod.class,pc.getIdPro());
        
        if(proizvod==null)return;
        
        try{ 
            em.getTransaction().begin();
            
            proizvod.setCijena(pc.getVal());
            em.getTransaction().commit();
        }
        finally{ 
            if(em.getTransaction().isActive())em.getTransaction().rollback();
        }
        return;
    }
    
    public static void mijenjajPopust(PopustCijena pc,EntityManager em,JMSContext context,JMSConsumer consumer,JMSProducer producer) throws JMSException{ 
         List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", pc.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return ;
        
        Korisnik korisnik=korisnici.get(0);
        
        
        
        podsistem2.PopustCijena pcc=new podsistem2.PopustCijena();
        pcc.setIzbor(3);
        pcc.setKime(pc.getKime());
        
        pcc.setIdPro(pc.getIdPro());
        ObjectMessage om1=context.createObjectMessage(pcc);
        producer.send(myQueue2,om1);
        ObjectMessage om2=(ObjectMessage)consumer.receive();
        pcc=(podsistem2.PopustCijena)om2.getObject();
        if(pcc.getVal()==-1)return;
        
        Proizvod proizvod=em.find(Proizvod.class,pc.getIdPro());
        
        if(proizvod==null)return;
        
        try{ 
            em.getTransaction().begin();
            
            proizvod.setPopust(pc.getVal());
            em.getTransaction().commit();
        }
        finally{ 
            if(em.getTransaction().isActive())em.getTransaction().rollback();
        }
        
        
    }
    
    public static void dodajNovacKorisniku(String korIme,double novac,EntityManager em){ 
        
        
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", korIme).getResultList();
        if(korisnici==null || korisnici.isEmpty())return;
        
        Korisnik korisnik=korisnici.get(0);
        
       
        
        
       try{
            em.getTransaction().begin();
            korisnik.setNovac(korisnik.getNovac()+novac);
            em.getTransaction().commit();
       }
       finally{ 
            if(em.getTransaction().isActive())em.getTransaction().rollback();
       }
       
       
    }
    
    
     public static void promjenaAdrese(PromjenaAdrese pa,EntityManager em){
       
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKime",Korisnik.class).setParameter("kime", pa.getKorisnickoIme()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return;
        
        Korisnik korisnik=korisnici.get(0);
        
       
        List<Grad>gradovi=em.createNamedQuery("Grad.findByNaziv",Grad.class).setParameter("naziv",pa.getGrad()).getResultList();
        
        if(gradovi==null || gradovi.isEmpty())return;
        
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
        
    }
     
    
    
    
}
