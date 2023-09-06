/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user2
 */
@Entity
@Table(name = "proizvod")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proizvod.findAll", query = "SELECT p FROM Proizvod p"),
    @NamedQuery(name = "Proizvod.findByIdPro", query = "SELECT p FROM Proizvod p WHERE p.idPro = :idPro"),
    @NamedQuery(name = "Proizvod.findByNaziv", query = "SELECT p FROM Proizvod p WHERE p.naziv = :naziv"),
    @NamedQuery(name = "Proizvod.findByOpis", query = "SELECT p FROM Proizvod p WHERE p.opis = :opis"),
    @NamedQuery(name = "Proizvod.findByCijena", query = "SELECT p FROM Proizvod p WHERE p.cijena = :cijena"),
    @NamedQuery(name = "Proizvod.findByPopust", query = "SELECT p FROM Proizvod p WHERE p.popust = :popust")})
public class Proizvod implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPro")
    private Integer idPro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Naziv")
    private String naziv;
    @Size(max = 45)
    @Column(name = "Opis")
    private String opis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cijena")
    private double cijena;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Popust")
    private Double popust;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPro")
    private List<Recenzija> recenzijaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proizvod")
    private List<SeNalazi> seNalaziList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "proizvod")
    private Prodaje prodaje;
    @JoinColumn(name = "idKat", referencedColumnName = "idKat")
    @ManyToOne
    private Kategorija idKat;

    public Proizvod() {
    }

    public Proizvod(Integer idPro) {
        this.idPro = idPro;
    }

    public Proizvod(Integer idPro, String naziv, double cijena) {
        this.idPro = idPro;
        this.naziv = naziv;
        this.cijena = cijena;
    }

    public Integer getIdPro() {
        return idPro;
    }

    public void setIdPro(Integer idPro) {
        this.idPro = idPro;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public Double getPopust() {
        return popust;
    }

    public void setPopust(Double popust) {
        this.popust = popust;
    }

    @XmlTransient
    public List<Recenzija> getRecenzijaList() {
        return recenzijaList;
    }

    public void setRecenzijaList(List<Recenzija> recenzijaList) {
        this.recenzijaList = recenzijaList;
    }

    @XmlTransient
    public List<SeNalazi> getSeNalaziList() {
        return seNalaziList;
    }

    public void setSeNalaziList(List<SeNalazi> seNalaziList) {
        this.seNalaziList = seNalaziList;
    }

    public Prodaje getProdaje() {
        return prodaje;
    }

    public void setProdaje(Prodaje prodaje) {
        this.prodaje = prodaje;
    }

    public Kategorija getIdKat() {
        return idKat;
    }

    public void setIdKat(Kategorija idKat) {
        this.idKat = idKat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPro != null ? idPro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proizvod)) {
            return false;
        }
        Proizvod other = (Proizvod) object;
        if ((this.idPro == null && other.idPro != null) || (this.idPro != null && !this.idPro.equals(other.idPro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Proizvod[ idPro=" + idPro + " ]";
    }
    
}
