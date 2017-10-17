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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 
 */
@Entity
@Table(name = "usuario_menu")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioMenu.findAll", query = "SELECT u FROM UsuarioMenu u")
    , @NamedQuery(name = "UsuarioMenu.findById", query = "SELECT u FROM UsuarioMenu u WHERE u.id = :id")
    , @NamedQuery(name = "UsuarioMenu.findByUid", query = "SELECT u FROM UsuarioMenu u WHERE u.uid = :uid")
    , @NamedQuery(name = "UsuarioMenu.findByNombre", query = "SELECT u FROM UsuarioMenu u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "UsuarioMenu.findByPath", query = "SELECT u FROM UsuarioMenu u WHERE u.path = :path")
    , @NamedQuery(name = "UsuarioMenu.findByIcono", query = "SELECT u FROM UsuarioMenu u WHERE u.icono = :icono")})
public class UsuarioMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 36)
    @Column(name = "id")
    @Id
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "uid")
    private int uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "path")
    private String path;
    @Size(max = 100)
    @Column(name = "icono")
    private String icono;

    public UsuarioMenu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
    
}
