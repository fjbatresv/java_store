/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Categoria;
import com.javastore.utils.Mensajes;
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
@Path("categoria")
public class CategoriaFacadeREST extends AbstractFacade<Categoria> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public CategoriaFacadeREST() {
        super(Categoria.class);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseHeader add(Categoria entity) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        List<Categoria> valid = em.createNamedQuery("Categoria.findByNombre", Categoria.class)
                .setParameter("nombre", entity.getNombre())
                .getResultList();
        if (valid.isEmpty()) {
            super.create(entity);
            respuesta.setMensaje(Mensajes.categoriaCreada);
        } else {
            respuesta.setCodigo(1);
            respuesta.setResultado(false);
            respuesta.setMensaje(Mensajes.categoriaNoCreada);
            this.logger.log(Level.WARNING, "Intento crear categoria repetida");
        }
        respuesta.setResponse(entity);
        return respuesta;
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseHeader edit(@PathParam("id") Integer id, Categoria entity) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        List<Categoria> valid = em.createNamedQuery("Categoria.findByNombre", Categoria.class)
                .setParameter("nombre", entity.getNombre())
                .getResultList();
        if ((valid.size() > 0 && valid.get(0).getId() == entity.getId()) || valid.isEmpty()) {
            super.edit(entity);
            respuesta.setMensaje(Mensajes.categoriaEditada);
        } else {
            respuesta.setCodigo(1);
            respuesta.setResultado(false);
            respuesta.setMensaje(Mensajes.categoriaNoEditada);
            this.logger.log(Level.WARNING, "Intento editar categoria repetida");
        }
        respuesta.setResponse(entity);
        return respuesta;
    }

    @DELETE
    @Path("{id}")
    public ResponseHeader remove(@PathParam("id") Integer id) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.categoriaEliminada);
        try {
            super.remove(super.find(id));
            this.logger.log(Level.INFO, "Categoria eliminada", id);
        } catch (Exception ex) {
            respuesta.setCodigo(1);
            respuesta.setResultado(false);
            respuesta.setMensaje(Mensajes.categoriaNoEliminada);
            this.logger.log(Level.SEVERE, "No se pudo eliminar la categoria", ex);
        }
        return respuesta;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Categoria find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Categoria> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Categoria> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
