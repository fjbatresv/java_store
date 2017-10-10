/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Usuario;
import com.javastore.utils.Crypto;
import com.javastore.utils.Mensajes;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author fjbatresv
 */
@Stateless
@Path("usuario")
public class UsuarioFacadeREST extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public UsuarioFacadeREST() {
        super(Usuario.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader add(Usuario entity) {
        ResponseHeader respuesta = new ResponseHeader();
        try {
            entity.setPassword(Crypto.sha1(entity.getPassword()));
            List<Usuario> tmp = em.createNamedQuery("Usuario.findByEmail", Usuario.class)
                    .setParameter("email", entity.getEmail())
                    .getResultList();
            if (!tmp.isEmpty()) {
                respuesta.setCodigo(1);
                respuesta.setMensaje(Mensajes.usuarioRepetido);
                respuesta.setResponse(null);
                respuesta.setResultado(false);
                this.logger.log(Level.INFO, "Usuario repetido");
            } else {
                super.create(entity);
                respuesta.setCodigo(0);
                respuesta.setMensaje(Mensajes.usuarioCreado);
                respuesta.setResponse(entity);
                respuesta.setResultado(true);
            }
        } catch (NoSuchAlgorithmException ex) {
            this.logger.log(Level.SEVERE, "Password no encriptado");
        }
        return respuesta;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader edit(@PathParam("id") Integer id, Usuario entity) {
        ResponseHeader respuesta = new ResponseHeader();
        Usuario valid = em.createNamedQuery("Usuario.findById", Usuario.class)
                .setParameter("id", id)
                .getSingleResult();
        if (entity.getPassword() != null && !entity.getPassword().isEmpty()
                && !entity.getPassword().equals(valid.getPassword())) {
            this.logger.log(Level.INFO, "Password modificado");
            try {
                entity.setPassword(Crypto.sha1(entity.getPassword()));
            } catch (NoSuchAlgorithmException ex) {
                this.logger.log(Level.SEVERE, "Password no encriptado", ex);
            }
        } else {
            entity.setPassword(valid.getPassword());
        }
        super.edit(entity);
        respuesta.setCodigo(0);
        respuesta.setMensaje(Mensajes.usuarioEditado);
        respuesta.setResponse(entity);
        respuesta.setResultado(true);
        return respuesta;
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader remove(@PathParam("id") Integer id) {
        ResponseHeader respuesta = new ResponseHeader();
        try {
            super.remove(super.find(id));
            respuesta.setCodigo(0);
            respuesta.setMensaje(Mensajes.usuarioEliminado);
            respuesta.setResultado(true);
            this.logger.log(Level.INFO, "Usuario " + String.valueOf(id) + " eliminado");
        } catch (Exception ex) {
            respuesta.setCodigo(1);
            respuesta.setMensaje(Mensajes.usuarioNoEliminado);
            respuesta.setResultado(false);
            this.logger.log(Level.WARNING, ex.getLocalizedMessage(), ex);
        }
        return respuesta;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Usuario find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Usuario> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
