/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package podsistem2;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author user2
 */
public class DohvProizvod implements Serializable {
   
   private List<Elem>proizvodi;
   private String kime;

    public List<Elem> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<Elem> proizvodi) {
        this.proizvodi = proizvodi;
    }

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }

 
}
