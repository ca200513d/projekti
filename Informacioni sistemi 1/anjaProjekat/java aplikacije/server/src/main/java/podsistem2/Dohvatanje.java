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
public class Dohvatanje implements Serializable{
    private int izbor;
    private String kime;
   

    public int getIzbor() {
        return izbor;
    }

    public void setIzbor(int izbor) {
        this.izbor = izbor;
    }

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }

   
}
