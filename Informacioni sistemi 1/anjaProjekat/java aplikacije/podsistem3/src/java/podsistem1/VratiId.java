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
public class VratiId implements Serializable {
    private int idGra;
    private String naziv;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getIdGra() {
        return idGra;
    }

    public void setIdGra(int idGra) {
        this.idGra = idGra;
    }
}
