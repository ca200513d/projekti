/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import entiteti.*;
import java.util.ArrayList;
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
import javax.persistence.Query;

/**
 *
 * @author user2
 */
public class Main {

    @Resource(lookup="jms/__defaultConnectionFactory")
    private static ConnectionFactory connectionFactory;
   
    @Resource(lookup="queue2")
    private static Queue myQueue;
   
    @Resource(lookup="queue3")
    private static Queue queue3;
   
    @Resource(lookup="topic1")
    private static Topic myTopic;
   
    public static void main(String[] args) {
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("podsistem2PU");
        EntityManager em=emf.createEntityManager();
       
       
       
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        JMSConsumer consumer=context.createConsumer(myQueue);
        try{
            while(true){
                ObjectMessage om=(ObjectMessage)consumer.receive();
                String povratnaPoruka=null;
                if(om.getObject() instanceof DohvProizvod){
                    DohvProizvod dp=(DohvProizvod)om.getObject();
                    Korisnik kupac=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme",dp.getKime()).getSingleResult();
                    if(kupac!=null){
                        List<Elem>elems=new ArrayList<>();
                        List<SeNalazi>stvari=em.createNamedQuery("SeNalazi.findByIdKor",SeNalazi.class).setParameter("idKor",kupac.getIdKor()).getResultList();
                        if(stvari!=null && !stvari.isEmpty()){
                            for(SeNalazi stvar:stvari){
                                Elem elem=new Elem();
                                elem.setCijena(stvar.getProizvod().getCijena());
                                elem.setPopust(stvar.getProizvod().getPopust());
                                elem.setKoProdaje(stvar.getProizvod().getProdaje().getIdKor().getIdKor());
                                elem.setIdPro(stvar.getProizvod().getIdPro());
                                elem.setKolicina(stvar.getKolicina());
                                elem.setNaziv(stvar.getProizvod().getNaziv());
                                KorisnikStvari ks=new KorisnikStvari();
                                ks.setkIme(stvar.getProizvod().getProdaje().getIdKor().getKIme());
                                ks.setSifra(stvar.getProizvod().getProdaje().getIdKor().getSifra());
                               
                                ks.setIme(stvar.getProizvod().getProdaje().getIdKor().getIme());
                                ks.setPrezime(stvar.getProizvod().getProdaje().getIdKor().getPrezime());
                                ks.setNovac(stvar.getProizvod().getProdaje().getIdKor().getNovac());
                                elem.setKs(ks);
                               
                                elems.add(elem);
                               
                                Korisnik vlasnik=stvar.getProizvod().getProdaje().getIdKor();
                                try{
                                    em.getTransaction().begin();
                                    double cijena=stvar.getProizvod().getCijena()-(stvar.getProizvod().getCijena()*(stvar.getProizvod().getPopust()/100));
                                    vlasnik.setNovac(vlasnik.getNovac()+cijena*stvar.getKolicina());
                                    em.remove(stvar);
                                    em.flush();
                                    em.getTransaction().commit();
                                }
                                finally{
                                    if(em.getTransaction().isActive())em.getTransaction();
                                }
                               
                            }
                        }
                        dp.setProizvodi(elems);
                        ObjectMessage vrati=context.createObjectMessage(dp);
                        producer.send(queue3,vrati);
                    }
                    continue;
                }
                else if(om.getObject() instanceof IdKor){
                    IdKor kor=(IdKor)om.getObject();
                    Korisnik korisnik=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme",kor.getKime()).getSingleResult();
                    if(korisnik!=null){
                        List<Korpa>korpe=em.createNamedQuery("Korpa.findByIdKor",Korpa.class).setParameter("idKor",korisnik.getIdKor()).getResultList();
                        if(korpe==null || korpe.isEmpty())kor.setPraznaKorpa(true);
                        else{
                            Korpa korpa=korpe.get(0);
                            if(korpa.getUkupnaCijena()==0)kor.setPraznaKorpa(true);
                            else if(korisnik.getNovac()<korpa.getUkupnaCijena()){
                                kor.setMozePlatiti(false);
                                kor.setPraznaKorpa(false);
                            }
                            else{
                                kor.setMozePlatiti(true);
                                kor.setPraznaKorpa(false);
                                kor.setUkupnaCijena(korpa.getUkupnaCijena());
                                try{
                                    em.getTransaction().begin();
                                   
                                    korisnik.setNovac(korisnik.getNovac()-korpa.getUkupnaCijena());
                                    korpa.setUkupnaCijena(0);
                                    em.getTransaction().commit();
                                }
                                finally{
                                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                                }
                            }
                        }
                       
                        ObjectMessage om1=context.createObjectMessage(kor);
                        producer.send(queue3,om1);
                    }
                    continue;
                }  
                else if(om.getObject() instanceof KorisnikStvari){
                    KorisnikStvari ks=(KorisnikStvari)om.getObject();
                    kreirajKorisnika(ks,em);
                }

                else if(om.getObject() instanceof Novac){
                    Novac n=(Novac)om.getObject();
                    double novac=n.getNovac();
                    String kime=n.getKorisnickoIme();
                   
                    dodajNovacKorisniku(kime,novac,em);
                }
                else{
                        if(om.getObject() instanceof KategorijaStvari){
                            KategorijaStvari ks=(KategorijaStvari)om.getObject();
                            povratnaPoruka=dodajKategoriju(ks,em);
                        }
                        if(om.getObject() instanceof ArtikalStvari){
                            ArtikalStvari as=(ArtikalStvari)om.getObject();
                            povratnaPoruka=dodajArtikal(as,em);
                        }
                       
                        if(om.getObject() instanceof PopustCijena){
                            PopustCijena pc=(PopustCijena)om.getObject();
                            int izbor=pc.getIzbor();
                            switch(izbor){
                                case 0:
                                    povratnaPoruka=mijenjajCijenu(pc,em);
                                    break;
                                case 1:
                                    povratnaPoruka=mijenjajPopust(pc,em);
                                    break;
                                   
                                case 3:
                                    List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", pc.getKime()).getResultList();
                                    if(korisnici==null || korisnici.isEmpty()){
                                        pc.setVal(-1);
                                        ObjectMessage om1=context.createObjectMessage(pc);
                                        producer.send(queue3,om1);
                                       
                                        continue;
   
                                    }
       
                                    Korisnik korisnik=korisnici.get(0);
                                    Prodaje prodaje=em.find(Prodaje.class,pc.getIdPro());
                                    if(prodaje==null){
                                        pc.setVal(-1);
                                        ObjectMessage om1=context.createObjectMessage(pc);
                                        producer.send(queue3,om1);
                                        continue;
                                    }
       
                                    if(!korisnik.equals(prodaje.getIdKor())){
                                        pc.setVal(-1);
                                        ObjectMessage om1=context.createObjectMessage(pc);
                                        producer.send(queue3,om1);
                                       
                                        continue;
                                    }
                                    pc.setVal(0);
                                    ObjectMessage om1=context.createObjectMessage(pc);
                                    producer.send(queue3,om1);
                                    continue;
                            }
                        }
                        if(om.getObject() instanceof dodajKorpa){
                            dodajKorpa dodaj=(dodajKorpa)om.getObject();
                           
                            int izbor=dodaj.getIzbor();
                            switch(izbor){
                                case 0:
                                    povratnaPoruka=dodajUKorpu(dodaj,em);
                                    break;
                                case 1:
                                    povratnaPoruka=izbaciIzKorpe(dodaj,em);
                                    break;
                            }
                        }
                        if(om.getObject() instanceof Dohvatanje){
                            Dohvatanje dohv=(Dohvatanje)om.getObject();
                           
                            int izbor=dohv.getIzbor();
                            switch(izbor){
                                case 0:
                                    povratnaPoruka=vratiKategorije(dohv,em);
                                    break;
                                case 1:
                                    povratnaPoruka=vratiProdaje(dohv,em);
                                    break;
                                case 2:
                                    povratnaPoruka=vratiKorpa(dohv,em);
                                    break;
                                   
                            }
                        }
                        if(om.getObject() instanceof Zaustavi){
                            break;
                        }
                       
                        TextMessage tm=context.createTextMessage(povratnaPoruka);
                        producer.send(myTopic, tm);
                       
                }
               

               


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
   
    public static void kreirajKorisnika(KorisnikStvari korStvari,EntityManager em){
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme",korStvari.getkIme()).getResultList();
       
        if(korisnici!=null && !korisnici.isEmpty())return;
       
        Korisnik korisnik=new Korisnik();
       
   
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
    }
   
    public static void dodajNovacKorisniku(String korIme,double novac, EntityManager em){
         List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", korIme).getResultList();
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
    public static String dodajKategoriju(KategorijaStvari ks,EntityManager em){
       
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", ks.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
        List<Kategorija>kategorije=em.createNamedQuery("Kategorija.findByNaziv",Kategorija.class).setParameter("naziv",ks.getNaziv()).getResultList();
       
        if(kategorije!=null && !kategorije.isEmpty())return "Kategorija sa zadatim nazivom vec postoji.";
       
        Kategorija kat=new Kategorija();
        kat.setNaziv(ks.getNaziv());
       
       
        if(ks.getNazivNadKat()!=null){
            List<Kategorija>nadkat=em.createNamedQuery("Kategorija.findByNaziv",Kategorija.class).setParameter("naziv",ks.getNazivNadKat()).getResultList();

            if(nadkat==null || nadkat.isEmpty())return "Zadata nadkategorija ne postoji.";  
           
            kat.setIdKatNad(nadkat.get(0));
        }
       
        try{
        em.getTransaction().begin();

        em.persist(kat);

        em.flush();

        em.getTransaction().commit();
        }
        finally{
            if(em.getTransaction().isActive())em.getTransaction().rollback();
        }
       
       
       
        return "Kreirana je nova kategorija.";
    }
   
   
    public static String dodajArtikal(ArtikalStvari as,EntityManager em){
       
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", as.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
       List<Kategorija>kat=em.createNamedQuery("Kategorija.findByNaziv",Kategorija.class).setParameter("naziv",as.getKategorija()).getResultList();
       if(kat==null || kat.isEmpty())return "Zadata kategorija ne postoji.";
       Kategorija kategorija=kat.get(0);
       Proizvod p=new Proizvod();
       p.setCijena(as.getCijena());
       p.setPopust(as.getPopust());
       p.setNaziv(as.getNaziv());
       if(as.getOpis()!=null)p.setOpis(as.getOpis());
       p.setIdKat(kategorija);
       
       Prodaje pp =new Prodaje();
       pp.setIdKor(korisnik);
       pp.setProizvod(p);
       
       
       try{
            em.getTransaction().begin();
           
            em.persist(p);
            em.flush();
            pp.setIdPro(p.getIdPro());
            em.persist(pp);
            em.flush();
           
            em.getTransaction().commit();
        }
        finally{
            if(em.getTransaction().isActive())em.getTransaction().rollback();
        }
       
        return "Kreiran je novi artikal sa identifikacionim brojem "+p.getIdPro()+".";
    }
   
    public static String mijenjajCijenu(PopustCijena pc,EntityManager em){
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", pc.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
        Proizvod proizvod=em.find(Proizvod.class,pc.getIdPro());
       
        if(proizvod==null)return "Ne postoji proizvod sa zadatim ID brojem.";
       
        Prodaje prodaje=em.find(Prodaje.class,pc.getIdPro());
        if(prodaje==null)return "Ne postoji proizvod sa zadatim ID brojem.";
       
        if(!korisnik.equals(prodaje.getIdKor()))return "Niste vlasnik proizvoda!";
       
        List<SeNalazi>senalaze=em.createNamedQuery("SeNalazi.findByIdPro",SeNalazi.class).setParameter("idPro",proizvod.getIdPro()).getResultList();
       
        List<Korpa>korpe;
       
       
        double cijenaStara=proizvod.getCijena();
        try{
            em.getTransaction().begin();
           
            proizvod.setCijena(pc.getVal());
            em.getTransaction().commit();
        }
        finally{
            if(em.getTransaction().isActive())em.getTransaction().rollback();
        }
         if(senalaze!=null && !senalaze.isEmpty()){
            for(SeNalazi sn:senalaze){
                korpe=em.createNamedQuery("Korpa.findByIdKor",Korpa.class).setParameter("idKor",sn.getKorisnik().getIdKor()).getResultList();
                if(korpe==null || korpe.isEmpty())continue;
                Korpa korpa=korpe.get(0);
                double staraCijena=cijenaStara-(cijenaStara*(proizvod.getPopust()/100));
                double vrati=korpa.getUkupnaCijena()-(staraCijena*sn.getKolicina());
                double cijena=pc.getVal()-(pc.getVal()*(proizvod.getPopust()/100));
                try{
                    em.getTransaction().begin();
                    korpa.setUkupnaCijena(vrati+(cijena*sn.getKolicina()));
                    em.getTransaction().commit();
                }
                 finally{
                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                }
            }
           }
        return "Cijena je promjenjena.";
    }
   
    public static String mijenjajPopust(PopustCijena pc,EntityManager em){
         List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", pc.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
        Proizvod proizvod=em.find(Proizvod.class,pc.getIdPro());
       
        if(proizvod==null)return "Ne postoji proizvod sa zadatim ID brojem.";
       
        Prodaje prodaje=em.find(Prodaje.class,pc.getIdPro());
        if(prodaje==null)return "Ne postoji proizvod sa zadatim ID brojem.";
       
        if(!korisnik.equals(prodaje.getIdKor()))return "Niste vlasnik proizvoda!";
       
        List<SeNalazi>senalaze=em.createNamedQuery("SeNalazi.findByIdPro",SeNalazi.class).setParameter("idPro",proizvod.getIdPro()).getResultList();
       
        List<Korpa>korpe;
        double popustStari=proizvod.getPopust();
        try{
            em.getTransaction().begin();
           
            proizvod.setPopust(pc.getVal());
            em.getTransaction().commit();
        }
        finally{
            if(em.getTransaction().isActive())em.getTransaction().rollback();
        }
        if(senalaze!=null && !senalaze.isEmpty()){
                for(SeNalazi sn:senalaze){
                   korpe=em.createNamedQuery("Korpa.findByIdKor",Korpa.class).setParameter("idKor",sn.getKorisnik().getIdKor()).getResultList();
                   if(korpe==null || korpe.isEmpty())continue;
                   Korpa korpa=korpe.get(0);
                   double staraCijena=proizvod.getCijena()-(proizvod.getCijena()*(popustStari/100));
                   double vrati=korpa.getUkupnaCijena()-(staraCijena*sn.getKolicina());
                   double cijena=proizvod.getCijena()-(proizvod.getCijena()*(pc.getVal()/100));
                   try{
                       em.getTransaction().begin();
                       korpa.setUkupnaCijena(vrati+(cijena*sn.getKolicina()));
                       em.getTransaction().commit();
                   }
                   finally{
                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                }
                   
                }
          }
       
        return "Popust je promjenjen.";
    }
   
   
    public static String dodajUKorpu(dodajKorpa dk,EntityManager em){
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", dk.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
        List<Korpa>korpe=em.createNamedQuery("Korpa.findByIdKor",Korpa.class).setParameter("idKor",korisnik.getIdKor()).getResultList();
        Korpa korpa;
       
        Proizvod pro=em.find(Proizvod.class,dk.getIdPro());
        if(pro==null)return "Proizvod ne postoji.";
        Query q = em.createNamedQuery("Prodaje.findByIdPro");
        q.setParameter("idPro", pro.getIdPro());
        List<Prodaje> lista = q.getResultList();
        if(lista == null || lista.size()==0) return " ";
        pro.setProdaje(lista.get(0));
       
        if(pro.getProdaje() == null) return "Test1";
        if(pro.getProdaje().getIdKor().equals(korisnik))return "Vi prodajete ovaj proizvod.";
        if(korpe==null || korpe.isEmpty()){
            korpa=new Korpa();
            korpa.setKorisnik(korisnik);
            korpa.setIdKor(korisnik.getIdKor());
            double cijena=pro.getCijena()-(pro.getCijena()*(pro.getPopust()/100));
            korpa.setUkupnaCijena(cijena*dk.getKolicina());
           
            SeNalazi sn=new SeNalazi();
            sn.setKorisnik(korisnik);
           
            sn.setProizvod(pro);
            sn.setSeNalaziPK(new SeNalaziPK(pro.getIdPro(),korisnik.getIdKor()));
            sn.setKolicina(dk.getKolicina());
            try{
                em.getTransaction().begin();
                em.persist(korpa);
                em.persist(sn);
                em.getTransaction().commit();
            }
            finally{
                if(em.getTransaction().isActive())em.getTransaction().rollback();
            }
        }
   
        else{
           
            korpa=korpe.get(0);
           
            List<SeNalazi>s=em.createQuery("SELECT S FROM SeNalazi S WHERE S.korisnik=:korisnik AND  S.proizvod=:proizvod",SeNalazi.class).setParameter("korisnik",korisnik).setParameter("proizvod",pro).getResultList();
            SeNalazi sn;
           
            if(s==null || s.isEmpty()){
                sn=new SeNalazi();
                sn.setKorisnik(korisnik);
                sn.setProizvod(pro);
                sn.setSeNalaziPK(new SeNalaziPK(pro.getIdPro(),korisnik.getIdKor()));
                sn.setKolicina(dk.getKolicina());
                try{
                    em.getTransaction().begin();
                    double cijena=pro.getCijena()-(pro.getCijena()*(pro.getPopust()/100));
                    korpa.setUkupnaCijena(korpa.getUkupnaCijena()+(cijena*dk.getKolicina()));
                    em.persist(sn);
                    em.getTransaction().commit();
                }
                finally{
                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                }
            }
            else{
                sn=s.get(0);
               
                try{
                    em.getTransaction().begin();
                    double cijena=pro.getCijena()-(pro.getCijena()*(pro.getPopust()/100));
                    korpa.setUkupnaCijena(korpa.getUkupnaCijena()+(cijena*dk.getKolicina()));
                    sn.setKolicina(sn.getKolicina()+dk.getKolicina());
                    em.getTransaction().commit();
                }
            finally{
                    if(em.getTransaction().isActive())em.getTransaction().rollback();
                }
            }
           
        }
            return "Dodat je artikal u korpu.";
           
       
       
    }
   
    public static String izbaciIzKorpe(dodajKorpa dk,EntityManager em){
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", dk.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
       
        Proizvod pro=em.find(Proizvod.class,dk.getIdPro());
        if(pro==null)return "Proizvod ne postoji.";
       
        List<SeNalazi>nalazi=em.createQuery("SELECT S FROM SeNalazi S WHERE S.korisnik=:korisnik AND S.proizvod=:proizvod",SeNalazi.class).setParameter("korisnik",korisnik).setParameter("proizvod",pro).getResultList();
       
        if(nalazi==null || nalazi.isEmpty())return "Korisnik nije ubacio zadati proizvod u korpu.";
       
        SeNalazi nalaziSe=nalazi.get(0);
       
        List<Korpa>korpe=em.createNamedQuery("Korpa.findByIdKor",Korpa.class).setParameter("idKor",korisnik.getIdKor()).getResultList();
        Korpa korpa=korpe.get(0);
       
        if(nalaziSe.getKolicina()<dk.getKolicina())return "Nema toliko proizvoda u korpi.";
        if(nalaziSe.getKolicina()==dk.getKolicina()){
            try{
                em.getTransaction().begin();
                double cijena=pro.getCijena()-(pro.getCijena()*(pro.getPopust()/100));
                korpa.setUkupnaCijena(korpa.getUkupnaCijena()-(cijena*dk.getKolicina()));
                List<SeNalazi>seNalaze=em.createQuery("SELECT S FROM SeNalazi S WHERE S.korisnik=:korisnik AND S.proizvod=:proizvod",SeNalazi.class).setParameter("korisnik",korisnik).setParameter("proizvod",pro).getResultList();
                if(!(seNalaze==null || seNalaze.isEmpty()))em.remove(seNalaze.get(0));
                em.getTransaction().commit();
                
            }
            finally{
                if(em.getTransaction().isActive())em.getTransaction().rollback();
            }
        }
        else{
           try{
                em.getTransaction().begin();
                double cijena=pro.getCijena()-(pro.getCijena()*(pro.getPopust()/100));
                korpa.setUkupnaCijena(korpa.getUkupnaCijena()-(cijena*dk.getKolicina()));
                nalaziSe.setKolicina(nalaziSe.getKolicina()-dk.getKolicina());
                em.getTransaction().commit();
            }
            finally{
                if(em.getTransaction().isActive())em.getTransaction().rollback();
            }
        }
        return "Obrisano.";
    }
   
    public static String vratiKategorije(Dohvatanje dohv,EntityManager em){
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", dohv.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
        List<Kategorija>kat=em.createNamedQuery("Kategorija.findAll",Kategorija.class).getResultList();
       
        if(kat==null || kat.isEmpty())return "Ne postoji nijedna kategorija.";
       
        StringBuffer sb=new StringBuffer();
        for(Kategorija kategorija: kat){
            sb.append(kategorija.getIdKat()+".");
            sb.append( kategorija.getNaziv()+"\n");
        }
        return sb.toString();
    }
   
   
    public static String vratiProdaje(Dohvatanje dohv,EntityManager em){
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", dohv.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
        List<Prodaje>prodaje=em.createQuery("SELECT P from Prodaje P WHERE P.idKor=:korisnik",Prodaje.class).setParameter("korisnik",korisnik).getResultList();
       
        if(prodaje==null || prodaje.isEmpty())return "Korisnik nista ne prodaje.";
       
        StringBuffer sb=new StringBuffer();
        for(Prodaje prod: prodaje){
            Proizvod pro=prod.getProizvod();
            sb.append("PROIZVOD BR."+pro.getIdPro()+": \n");
            sb.append("Ime: "+pro.getNaziv()+"\n");
            sb.append("Cijena: "+pro.getCijena()+"\n");
            if(pro.getOpis()!=null){
                sb.append("Opis: "+pro.getOpis()+"\n");
            }
            sb.append("Na popustu: "+pro.getPopust()+"% \n");
            Kategorija kat=pro.getIdKat();
            sb.append("Kategorija: "+kat.getNaziv()+"\n\n");
        }
        return sb.toString();
       
    }
   
    public static String vratiKorpa(Dohvatanje dohv,EntityManager em){
        List<Korisnik>korisnici=em.createNamedQuery("Korisnik.findByKIme",Korisnik.class).setParameter("kIme", dohv.getKime()).getResultList();
        if(korisnici==null || korisnici.isEmpty())return "Korisnik sa zadatim korisnickim imenom ne postoji.";
       
        Korisnik korisnik=korisnici.get(0);
       
       
       
        List<Korpa>korpe=em.createNamedQuery("Korpa.findByIdKor",Korpa.class).setParameter("idKor",korisnik.getIdKor()).getResultList();
       
        if(korpe==null || korpe.isEmpty())return "Korpa je prazna.";
       
        Korpa korpa=korpe.get(0);
       
        List<SeNalazi>nalazi=em.createNamedQuery("SeNalazi.findByIdKor",SeNalazi.class).setParameter("idKor",korisnik.getIdKor()).getResultList();
       
        if(nalazi==null || nalazi.isEmpty())return "Korpa je prazna";
       
        StringBuffer sb=new StringBuffer();
        for(SeNalazi nalaziSe:nalazi){
            Proizvod pro=nalaziSe.getProizvod();
            sb.append("PROIZVOD BR."+pro.getIdPro()+": \n");
            sb.append("Ime: "+pro.getNaziv()+"\n");
            sb.append("Cijena: "+pro.getCijena()+"\n");
            if(pro.getOpis()!=null){
                sb.append("Opis: "+pro.getOpis()+"\n");
            }
            sb.append("Na popustu: "+pro.getPopust()+"% \n");
            Kategorija kat=pro.getIdKat();
            sb.append("Kategorija: "+kat.getNaziv()+"\n");
            sb.append("Kolicina: "+nalaziSe.getKolicina()+"\n\n");
        }
        return sb.toString();
    }
   
}