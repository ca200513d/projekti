
package podsistem2;
import java.io.Serializable;
import podsistem2.KorisnikStvari;

 public class Elem implements Serializable{ 
    int idPro;
    String naziv;
    double cijena;
    double popust;
    int kolicina;
    KorisnikStvari ks;
    int koProdaje;

    public int getKoProdaje() {
        return koProdaje;
    }

    public void setKoProdaje(int koProdaje) {
        this.koProdaje = koProdaje;
    }

    public KorisnikStvari getKs() {
        return ks;
    }

    public void setKs(KorisnikStvari ks) {
        this.ks = ks;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public int getIdPro() {
        return idPro;
    }

    public void setIdPro(int idPro) {
        this.idPro = idPro;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public double getPopust() {
        return popust;
    }

    public void setPopust(double popust) {
        this.popust = popust;
    }
} 