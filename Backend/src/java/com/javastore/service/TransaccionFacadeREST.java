/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.EstadoTransaccion;
import com.javastore.entities.FlujoTransaccion;
import com.javastore.entities.Transaccion;
import com.javastore.utils.Mensajes;
import java.util.Date;
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
@Path("transaccion")
public class TransaccionFacadeREST extends AbstractFacade<Transaccion> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public TransaccionFacadeREST() {
        super(Transaccion.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Transaccion entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Transaccion entity) {
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
    public Transaccion find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Transaccion> findAll() {
        return super.findAll();
    }

    @GET
    @Path("cliente/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Transaccion> byCliente(@PathParam("id") Integer id) {
        this.logger.log(Level.INFO, "Transacciones para el cliente " + String.valueOf(id));
        return em.createNamedQuery("Transaccion.findByClienteId", Transaccion.class)
                .setParameter("id", id)
                .getResultList();
    }
    
    @PUT
    @Path("cobro/{id}/{uid}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader cobrar(@PathParam("id") Integer id, @PathParam("uid") Integer uid){
        ResponseHeader respuesta = new ResponseHeader();
        Transaccion transaccion = super.find(id);
        EstadoTransaccion estado = em.createNamedQuery("EstadoTransaccion.findByNombre", EstadoTransaccion.class)
                .setParameter("nombre", "Pagada")
                .getSingleResult();
        transaccion.setEstadoId(estado);
        FlujoTransaccion flujo = new FlujoTransaccion();
        flujo.setEstadoId(estado);
        flujo.setTransaccionId(transaccion);
        flujo.setComentario(new Date().toString() + " | Por el usuario: " + String.valueOf(uid));
        em.merge(transaccion);
        em.persist(flujo);
        em.flush();
        this.logger.log(Level.INFO, "Transaccion pagada");
        respuesta.setCodigo(0);
        respuesta.setResponse(transaccion);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.transaccionPagada.replace("%id%", String.valueOf(id)));
        return respuesta;
    }
    
    @PUT
    @Path("enviar/{id}/{uid}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader enviar(@PathParam("id") Integer id, @PathParam("uid") Integer uid){
        ResponseHeader respuesta = new ResponseHeader();
        Transaccion transaccion = super.find(id);
        EstadoTransaccion estado = em.createNamedQuery("EstadoTransaccion.findByNombre", EstadoTransaccion.class)
                .setParameter("nombre", "Enviada")
                .getSingleResult();
        transaccion.setEstadoId(estado);
        FlujoTransaccion flujo = new FlujoTransaccion();
        flujo.setEstadoId(estado);
        flujo.setTransaccionId(transaccion);
        flujo.setComentario(new Date().toString() + " | Por el usuario: " + String.valueOf(uid));
        em.merge(transaccion);
        em.persist(flujo);
        em.flush();
        this.logger.log(Level.INFO, "Transaccion enviada");
        respuesta.setCodigo(0);
        respuesta.setResponse(transaccion);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.transaccionEnviada.replace("%id%", String.valueOf(id)));
        return respuesta;
    }
    
    @PUT
    @Path("entrega/{id}/{uid}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader entrega(@PathParam("id") Integer id, @PathParam("uid") Integer uid){
        ResponseHeader respuesta = new ResponseHeader();
        Transaccion transaccion = super.find(id);
        EstadoTransaccion estado = em.createNamedQuery("EstadoTransaccion.findByNombre", EstadoTransaccion.class)
                .setParameter("nombre", "Entregada")
                .getSingleResult();
        transaccion.setEstadoId(estado);
        FlujoTransaccion flujo = new FlujoTransaccion();
        flujo.setEstadoId(estado);
        flujo.setTransaccionId(transaccion);
        flujo.setComentario(new Date().toString() + " | Por el usuario: " + String.valueOf(uid));
        em.merge(transaccion);
        em.persist(flujo);
        em.flush();
        this.logger.log(Level.INFO, "Transaccion entregada");
        respuesta.setCodigo(0);
        respuesta.setResponse(transaccion);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.transaccionEntregada.replace("%id%", String.valueOf(id)));
        return respuesta;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Transaccion> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
