/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.javimg.pilotosmotociclismo.entities;

import es.javimg.pilotosmotociclismo.entities.Piloto;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author usuario
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "EQUIPO")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Equipo.findAll", query = "SELECT e FROM Equipo e"),
    @javax.persistence.NamedQuery(name = "Equipo.findById", query = "SELECT e FROM Equipo e WHERE e.id = :id"),
    @javax.persistence.NamedQuery(name = "Equipo.findByCodigoEquipo", query = "SELECT e FROM Equipo e WHERE e.codigoEquipo = :codigoEquipo"),
    @javax.persistence.NamedQuery(name = "Equipo.findByNombre", query = "SELECT e FROM Equipo e WHERE e.nombre = :nombre")})
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Column(name = "CODIGO_EQUIPO")
    private String codigoEquipo;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.OneToMany(mappedBy = "equipo")
    private Collection<Piloto> pilotoCollection;

    public Equipo() {
    }

    public Equipo(Integer id) {
        this.id = id;
    }

    public Equipo(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoEquipo() {
        return codigoEquipo;
    }

    public void setCodigoEquipo(String codigoEquipo) {
        this.codigoEquipo = codigoEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Piloto> getPilotoCollection() {
        return pilotoCollection;
    }

    public void setPilotoCollection(Collection<Piloto> pilotoCollection) {
        this.pilotoCollection = pilotoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.javimg.pilotosmotociclismo.entities.Equipo[ id=" + id + " ]";
    }
    
}
