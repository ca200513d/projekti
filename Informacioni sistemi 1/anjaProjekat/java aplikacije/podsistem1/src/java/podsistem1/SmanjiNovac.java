/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem1;

import java.io.Serializable;

/**
 *
 * @author user2
 */
public class SmanjiNovac implements Serializable{
    int idKor;
    double kolicina;
    boolean smanji;

    public boolean isSmanji() {
        return smanji;
    }

    public void setSmanji(boolean smanji) {
        this.smanji = smanji;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }
}
