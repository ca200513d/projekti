package entiteti;

import entiteti.Proizvod;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T11:45:37")
@StaticMetamodel(Recenzija.class)
public class Recenzija_ { 

    public static volatile SingularAttribute<Recenzija, Integer> idRec;
    public static volatile SingularAttribute<Recenzija, Proizvod> idPro;
    public static volatile SingularAttribute<Recenzija, Integer> ocjena;
    public static volatile SingularAttribute<Recenzija, String> opis;

}