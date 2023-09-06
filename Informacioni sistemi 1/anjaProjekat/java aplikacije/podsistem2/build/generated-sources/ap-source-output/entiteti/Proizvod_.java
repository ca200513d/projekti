package entiteti;

import entiteti.Kategorija;
import entiteti.Prodaje;
import entiteti.Recenzija;
import entiteti.SeNalazi;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T11:45:37")
@StaticMetamodel(Proizvod.class)
public class Proizvod_ { 

    public static volatile ListAttribute<Proizvod, Recenzija> recenzijaList;
    public static volatile SingularAttribute<Proizvod, Integer> idPro;
    public static volatile SingularAttribute<Proizvod, Kategorija> idKat;
    public static volatile SingularAttribute<Proizvod, String> naziv;
    public static volatile SingularAttribute<Proizvod, Prodaje> prodaje;
    public static volatile SingularAttribute<Proizvod, Double> popust;
    public static volatile ListAttribute<Proizvod, SeNalazi> seNalaziList;
    public static volatile SingularAttribute<Proizvod, Double> cijena;
    public static volatile SingularAttribute<Proizvod, String> opis;

}