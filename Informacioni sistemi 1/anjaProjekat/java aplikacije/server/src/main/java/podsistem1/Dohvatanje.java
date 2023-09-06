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
public class Dohvatanje implements Serializable{
    
    private int dohv;
    private String kime;
  

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }

   

    public int getDohv() {
        return dohv;
    }

    public void setDohv(int dohv) {
        this.dohv = dohv;
    }
    
    
}
