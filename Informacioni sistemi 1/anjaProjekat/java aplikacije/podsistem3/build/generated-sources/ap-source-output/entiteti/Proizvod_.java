package entiteti;

import entiteti.Stavka;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T22:02:42")
@StaticMetamodel(Proizvod.class)
public class Proizvod_ { 

    public static volatile SingularAttribute<Proizvod, Integer> idPro;
    public static volatile SingularAttribute<Proizvod, String> naziv;
    public static volatile ListAttribute<Proizvod, Stavka> stavkaList;
    public static volatile SingularAttribute<Proizvod, Double> popust;
    public static volatile SingularAttribute<Proizvod, Double> cijena;

}