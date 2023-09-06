package entiteti;

import entiteti.Narudzbina;
import entiteti.Proizvod;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T22:02:42")
@StaticMetamodel(Stavka.class)
public class Stavka_ { 

    public static volatile SingularAttribute<Stavka, Narudzbina> idNar;
    public static volatile SingularAttribute<Stavka, Integer> idSta;
    public static volatile SingularAttribute<Stavka, Double> jedinicnaCijena;
    public static volatile SingularAttribute<Stavka, Proizvod> idPro;
    public static volatile SingularAttribute<Stavka, Integer> kolicina;

}