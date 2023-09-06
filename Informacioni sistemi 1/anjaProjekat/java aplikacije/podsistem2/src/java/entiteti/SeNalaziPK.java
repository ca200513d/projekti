/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user2
 */
@Embeddable
public class SeNalaziPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idPro")
    private int idPro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idKor")
    private int idKor;

    public SeNalaziPK() {
    }

    public SeNalaziPK(int idPro, int idKor) {
        this.idPro = idPro;
        this.idKor = idKor;
    }

    public int getIdPro() {
        return idPro;
    }

    public void setIdPro(int idPro) {
        this.idPro = idPro;
    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPro;
        hash += (int) idKor;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeNalaziPK)) {
            return false;
        }
        SeNalaziPK other = (SeNalaziPK) object;
        if (this.idPro != other.idPro) {
            return false;
        }
        if (this.idKor != other.idKor) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.SeNalaziPK[ idPro=" + idPro + ", idKor=" + idKor + " ]";
    }
    
}
