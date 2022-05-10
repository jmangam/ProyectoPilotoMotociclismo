/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.javimg.pilotosmotociclismo.entities;

import es.javimg.pilotosmotociclismo.entities.Equipo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author usuario
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "PILOTO")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Piloto.findAll", query = "SELECT p FROM Piloto p"),
    @javax.persistence.NamedQuery(name = "Piloto.findById", query = "SELECT p FROM Piloto p WHERE p.id = :id"),
    @javax.persistence.NamedQuery(name = "Piloto.findByNombre", query = "SELECT p FROM Piloto p WHERE p.nombre = :nombre"),
    @javax.persistence.NamedQuery(name = "Piloto.findByApellidos", query = "SELECT p FROM Piloto p WHERE p.apellidos = :apellidos"),
    @javax.persistence.NamedQuery(name = "Piloto.findByEmail", query = "SELECT p FROM Piloto p WHERE p.email = :email"),
    @javax.persistence.NamedQuery(name = "Piloto.findByFechaNacimiento", query = "SELECT p FROM Piloto p WHERE p.fechaNacimiento = :fechaNacimiento"),
    @javax.persistence.NamedQuery(name = "Piloto.findByNumTitulos", query = "SELECT p FROM Piloto p WHERE p.numTitulos = :numTitulos"),
    @javax.persistence.NamedQuery(name = "Piloto.findBySalario", query = "SELECT p FROM Piloto p WHERE p.salario = :salario"),
    @javax.persistence.NamedQuery(name = "Piloto.findByCampeon", query = "SELECT p FROM Piloto p WHERE p.campeon = :campeon"),
    @javax.persistence.NamedQuery(name = "Piloto.findByEstiloPilotaje", query = "SELECT p FROM Piloto p WHERE p.estiloPilotaje = :estiloPilotaje"),
    @javax.persistence.NamedQuery(name = "Piloto.findByFoto", query = "SELECT p FROM Piloto p WHERE p.foto = :foto")})
public class Piloto implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "APELLIDOS")
    private String apellidos;
    @javax.persistence.Column(name = "EMAIL")
    private String email;
    @javax.persistence.Column(name = "FECHA_NACIMIENTO")
    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNacimiento;
    @javax.persistence.Column(name = "NUM_TITULOS")
    private Short numTitulos;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @javax.persistence.Column(name = "SALARIO")
    private BigDecimal salario;
    @javax.persistence.Column(name = "CAMPEON")
    private Boolean campeon;
    @javax.persistence.Column(name = "ESTILO_PILOTAJE")
    private Character estiloPilotaje;
    @javax.persistence.Column(name = "FOTO")
    private String foto;
    @javax.persistence.JoinColumn(name = "EQUIPO", referencedColumnName = "ID")
    @javax.persistence.ManyToOne
    private Equipo equipo;

    public Piloto() {
    }

    public Piloto(Integer id) {
        this.id = id;
    }

    public Piloto(Integer id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Short getNumTitulos() {
        return numTitulos;
    }

    public void setNumTitulos(Short numTitulos) {
        this.numTitulos = numTitulos;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Boolean getCampeon() {
        return campeon;
    }

    public void setCampeon(Boolean campeon) {
        this.campeon = campeon;
    }

    public Character getEstiloPilotaje() {
        return estiloPilotaje;
    }

    public void setEstiloPilotaje(Character estiloPilotaje) {
        this.estiloPilotaje = estiloPilotaje;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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
        if (!(object instanceof Piloto)) {
            return false;
        }
        Piloto other = (Piloto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.javimg.pilotosmotociclismo.entities.Piloto[ id=" + id + " ]";
    }
    
}
