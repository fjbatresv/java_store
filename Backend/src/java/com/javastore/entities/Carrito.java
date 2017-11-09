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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 
 * Esta clase amarra la relacion del BEAN/POJO con la tabla de mysql
 */
@Entity
@Table(name = "carrito")
@XmlRootElement
//Las namedQueries son consultas previamente creadas para consultar la base de datos.
@NamedQueries({
    @NamedQuery(name = "Carrito.findAll", query = "SELECT c FROM Carrito c")
    , @NamedQuery(name = "Carrito.findById", query = "SELECT c FROM Carrito c WHERE c.id = :id")
    , @NamedQuery(name = "Carrito.findByPrecio", query = "SELECT c FROM Carrito c WHERE c.precio = :precio")
    , @NamedQuery(name = "Carrito.findByClientId", query = "SELECT c FROM Carrito c WHERE c.clienteId.id = :id")})
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio")
    private double precio;
    //Por medio de un JoinColumn hacemos referencia que este campo es una llave foranea
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente clienteId;
    //Por medio de un JoinColumn hacemos referencia que este campo es una llave foranea
    @JoinColumn(name = "producto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Producto productoId;

    public Carrito() {
    }

    public Carrito(Integer id) {
        this.id = id;
    }

    public Carrito(Integer id, double precio) {
        this.id = id;
        this.precio = precio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Cliente getClienteId() {
        return clienteId;
    }

    public void setClienteId(Cliente clienteId) {
        this.clienteId = clienteId;
    }

    public Producto getProductoId() {
        return productoId;
    }

    public void setProductoId(Producto productoId) {
        this.productoId = productoId;
    }

    //Genera un indice para que el frontend y backend usen la misma variable
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    //Nos permite validar si dos objetos en java son en realidad el mismo en la base de datos;
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carrito)) {
            return false;
        }
        Carrito other = (Carrito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    //Nos devuelve una forma custom del tostring de este objeto
    @Override
    public String toString() {
        return "com.javastore.entities.Carrito[ id=" + id + " ]";
    }

}
