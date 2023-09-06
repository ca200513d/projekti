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

public class KorisnikStvari implements Serializable {
    private String kIme;
    private String sifra;
    private String ime;
    private String prezime;
    private String adresa;
    private double novac;
    private String idGra;

    public String getkIme() {
        return kIme;
    }

    public void setkIme(String kIme) {
        this.kIme = kIme;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public double getNovac() {
        return novac;
    }

    public void setNovac(double novac) {
        this.novac = novac;
    }

    public String getIdGra() {
        return idGra;
    }

    public void setIdGra(String idGra) {
        this.idGra = idGra;
    }
    
}
