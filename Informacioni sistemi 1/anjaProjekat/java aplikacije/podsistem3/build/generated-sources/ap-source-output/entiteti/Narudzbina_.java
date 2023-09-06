package entiteti;

import entiteti.Grad;
import entiteti.Korisnik;
import entiteti.Stavka;
import entiteti.Transakcija;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T22:02:42")
@StaticMetamodel(Narudzbina.class)
public class Narudzbina_ { 

    public static volatile SingularAttribute<Narudzbina, Date> vrijeme;
    public static volatile SingularAttribute<Narudzbina, Integer> idNar;
    public static volatile SingularAttribute<Narudzbina, Korisnik> idKor;
    public static volatile SingularAttribute<Narudzbina, Transakcija> transakcija;
    public static volatile SingularAttribute<Narudzbina, String> adresa;
    public static volatile ListAttribute<Narudzbina, Stavka> stavkaList;
    public static volatile SingularAttribute<Narudzbina, Double> ukupnaCijena;
    public static volatile SingularAttribute<Narudzbina, Grad> idGra;

}