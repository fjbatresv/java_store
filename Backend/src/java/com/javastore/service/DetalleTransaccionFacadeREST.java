/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.entities.DetalleTransaccion;
import com.javastore.entities.Producto;
import java.util.List;
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
@Path("detalle-transaccion")
public class DetalleTransaccionFacadeREST extends AbstractFacade<DetalleTransaccion> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public DetalleTransaccionFacadeREST() {
        super(DetalleTransaccion.class);
    }

    @GET
    @Path("transaccion/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<DetalleTransaccion> byTransaccion(@PathParam("id") Integer id) {
        return em.createNamedQuery("DetalleTransaccion.findByTransaccionId", DetalleTransaccion.class)
                .setParameter("id", id)
                .getResultList();
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(DetalleTransaccion entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, DetalleTransaccion entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public DetalleTransaccion find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<DetalleTransaccion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("recent")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Producto> recent() {
        return em.createNamedQuery("DetalleTransaccion.recent", Producto.class)
                .setMaxResults(20)
                .getResultList();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<DetalleTransaccion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
