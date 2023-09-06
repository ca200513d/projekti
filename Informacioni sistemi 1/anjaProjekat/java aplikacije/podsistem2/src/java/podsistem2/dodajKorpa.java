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
public class dodajKorpa implements Serializable{
    
    private String kime;
    private int idPro; 
    private int kolicina;
    private int izbor;

    public int getIzbor() {
        return izbor;
    }

    public void setIzbor(int izbor) {
        this.izbor = izbor;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }

    

    public int getIdPro() {
        return idPro;
    }

    public void setIdPro(int idPro) {
        this.idPro = idPro;
    }
    
}
