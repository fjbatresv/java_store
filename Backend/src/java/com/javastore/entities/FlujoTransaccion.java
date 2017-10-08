/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author fjbatresv
 */
@Entity
@Table(name = "flujo_transaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlujoTransaccion.findAll", query = "SELECT f FROM FlujoTransaccion f")
    , @NamedQuery(name = "FlujoTransaccion.findById", query = "SELECT f FROM FlujoTransaccion f WHERE f.id = :id")})
public class FlujoTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "comentario")
    private String comentario;
    @JoinColumn(name = "transaccion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Transaccion transaccionId;
    @JoinColumn(name = "estado_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EstadoTransaccion estadoId;

    public FlujoTransaccion() {
    }

    public FlujoTransaccion(Integer id) {
        this.id = id;
    }

    public FlujoTransaccion(Integer id, String comentario) {
        this.id = id;
        this.comentario = comentario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Transaccion getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Transaccion transaccionId) {
        this.transaccionId = transaccionId;
    }

    public EstadoTransaccion getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(EstadoTransaccion estadoId) {
        this.estadoId = estadoId;
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
        if (!(object instanceof FlujoTransaccion)) {
            return false;
        }
        FlujoTransaccion other = (FlujoTransaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.javastore.entities.FlujoTransaccion[ id=" + id + " ]";
    }
    
}
