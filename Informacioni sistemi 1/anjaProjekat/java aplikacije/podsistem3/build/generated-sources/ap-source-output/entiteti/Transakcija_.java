package entiteti;

import entiteti.Narudzbina;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T22:02:42")
@StaticMetamodel(Transakcija.class)
public class Transakcija_ { 

    public static volatile SingularAttribute<Transakcija, Double> suma;
    public static volatile SingularAttribute<Transakcija, Date> vrijeme;
    public static volatile SingularAttribute<Transakcija, Integer> idNar;
    public static volatile SingularAttribute<Transakcija, Narudzbina> narudzbina;

}