package entiteti;

import entiteti.Korisnik;
import entiteti.Narudzbina;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T22:02:42")
@StaticMetamodel(Grad.class)
public class Grad_ { 

    public static volatile ListAttribute<Grad, Narudzbina> narudzbinaList;
    public static volatile SingularAttribute<Grad, String> naziv;
    public static volatile SingularAttribute<Grad, Integer> idGra;
    public static volatile ListAttribute<Grad, Korisnik> korisnikList;

}