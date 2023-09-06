/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user2
 */
@Entity
@Table(name = "stavka")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stavka.findAll", query = "SELECT s FROM Stavka s"),
    @NamedQuery(name = "Stavka.findByIdSta", query = "SELECT s FROM Stavka s WHERE s.idSta = :idSta"),
    @NamedQuery(name = "Stavka.findByKolicina", query = "SELECT s FROM Stavka s WHERE s.kolicina = :kolicina"),
    @NamedQuery(name = "Stavka.findByJedinicnaCijena", query = "SELECT s FROM Stavka s WHERE s.jedinicnaCijena = :jedinicnaCijena")})
public class Stavka implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSta")
    private Integer idSta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Kolicina")
    private int kolicina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "jedinicnaCijena")
    private double jedinicnaCijena;
    @JoinColumn(name = "idNar", referencedColumnName = "idNar")
    @ManyToOne(optional = false)
    private Narudzbina idNar;
    @JoinColumn(name = "idPro", referencedColumnName = "idPro")
    @ManyToOne(optional = false)
    private Proizvod idPro;

    public Stavka() {
    }

    public Stavka(Integer idSta) {
        this.idSta = idSta;
    }

    public Stavka(Integer idSta, int kolicina, double jedinicnaCijena) {
        this.idSta = idSta;
        this.kolicina = kolicina;
        this.jedinicnaCijena = jedinicnaCijena;
    }

    public Integer getIdSta() {
        return idSta;
    }

    public void setIdSta(Integer idSta) {
        this.idSta = idSta;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public double getJedinicnaCijena() {
        return jedinicnaCijena;
    }

    public void setJedinicnaCijena(double jedinicnaCijena) {
        this.jedinicnaCijena = jedinicnaCijena;
    }

    public Narudzbina getIdNar() {
        return idNar;
    }

    public void setIdNar(Narudzbina idNar) {
        this.idNar = idNar;
    }

    public Proizvod getIdPro() {
        return idPro;
    }

    public void setIdPro(Proizvod idPro) {
        this.idPro = idPro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSta != null ? idSta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stavka)) {
            return false;
        }
        Stavka other = (Stavka) object;
        if ((this.idSta == null && other.idSta != null) || (this.idSta != null && !this.idSta.equals(other.idSta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Stavka[ idSta=" + idSta + " ]";
    }
    
}
