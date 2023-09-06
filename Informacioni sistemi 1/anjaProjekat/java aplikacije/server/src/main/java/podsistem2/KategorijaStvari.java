/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import java.io.Serializable;

/**
 *
 * @author user2
 */
public class KategorijaStvari implements Serializable {
   
    private String naziv;
    private String nazivNadKat;
    private String kime;
   

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }


    public String getNazivNadKat() {
        return nazivNadKat;
    }

    public void setNazivNadKat(String nazivNadKat) {
        this.nazivNadKat = nazivNadKat;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
}
