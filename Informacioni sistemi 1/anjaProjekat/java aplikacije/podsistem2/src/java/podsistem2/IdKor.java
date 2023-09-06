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
public class IdKor implements Serializable {
    boolean mozePlatiti=false;
    String kime;

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }
    boolean praznaKorpa;
    double ukupnaCijena;

    public double getUkupnaCijena() {
        return ukupnaCijena;
    }

    public void setUkupnaCijena(double ukupnaCijena) {
        this.ukupnaCijena = ukupnaCijena;
    }

    public boolean isPraznaKorpa() {
        return praznaKorpa;
    }

    public void setPraznaKorpa(boolean praznaKorpa) {
        this.praznaKorpa = praznaKorpa;
    }

    public boolean isMozePlatiti() {
        return mozePlatiti;
    }

    public void setMozePlatiti(boolean mozePlatiti) {
        this.mozePlatiti = mozePlatiti;
    }

  
}
