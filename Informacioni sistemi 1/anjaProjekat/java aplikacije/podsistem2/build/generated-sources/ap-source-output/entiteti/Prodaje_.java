package entiteti;

import entiteti.Korisnik;
import entiteti.Proizvod;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T11:45:37")
@StaticMetamodel(Prodaje.class)
public class Prodaje_ { 

    public static volatile SingularAttribute<Prodaje, Integer> idPro;
    public static volatile SingularAttribute<Prodaje, Korisnik> idKor;
    public static volatile SingularAttribute<Prodaje, Proizvod> proizvod;

}