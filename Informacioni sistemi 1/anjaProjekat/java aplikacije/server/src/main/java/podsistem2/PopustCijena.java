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
public class PopustCijena implements Serializable{
    
    private int izbor;
    private int idPro;
    private String kime;
    private double val;

    public int getIzbor() {
        return izbor;
    }

    public void setIzbor(int izbor) {
        this.izbor = izbor;
    }

    public int getIdPro() {
        return idPro;
    }

    public void setIdPro(int idPro) {
        this.idPro = idPro;
    }

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
    
    
}
