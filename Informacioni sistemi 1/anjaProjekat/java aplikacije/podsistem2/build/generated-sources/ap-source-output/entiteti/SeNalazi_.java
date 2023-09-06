package entiteti;

import entiteti.Korisnik;
import entiteti.Proizvod;
import entiteti.SeNalaziPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-21T11:45:37")
@StaticMetamodel(SeNalazi.class)
public class SeNalazi_ { 

    public static volatile SingularAttribute<SeNalazi, SeNalaziPK> seNalaziPK;
    public static volatile SingularAttribute<SeNalazi, Integer> kolicina;
    public static volatile SingularAttribute<SeNalazi, Proizvod> proizvod;
    public static volatile SingularAttribute<SeNalazi, Korisnik> korisnik;

}