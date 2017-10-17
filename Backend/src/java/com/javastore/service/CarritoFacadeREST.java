/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.dtos.TextoDTO;
import com.javastore.entities.Carrito;
import com.javastore.entities.Cliente;
import com.javastore.entities.DetalleTransaccion;
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
 * @author 
 */
@Stateless
@Path("carrito")
public class CarritoFacadeREST extends AbstractFacade<Carrito> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public CarritoFacadeREST() {
        super(Carrito.class);
    }

    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader add(Carrito entity, @PathParam("id") Integer id) {
        ResponseHeader respuesta = new ResponseHeader();
        Cliente cliente = em.createNamedQuery("Cliente.findById", Cliente.class)
                .setParameter("id", id)
                .getSingleResult();
        entity.setClienteId(cliente);
        super.create(entity);
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setResponse(entity);
        respuesta.setMensaje(Mensajes.carritoAgregado);
        return respuesta;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader checkout(@PathParam("id") Integer id, TextoDTO dto) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        Cliente cliente = em.createNamedQuery("Cliente.findById", Cliente.class)
                .setParameter("id", id)
                .getSingleResult();
        EstadoTransaccion estadoId = em.createNamedQuery("EstadoTransaccion.findByNombre", EstadoTransaccion.class)
                .setParameter("nombre", "Nueva")
                .getSingleResult();
        Transaccion transaccion = new Transaccion();
        transaccion.setDestino(dto.getTexto());
        transaccion.setClienteId(cliente);
        transaccion.setEstadoId(estadoId);
        em.persist(transaccion);
        em.flush();
        FlujoTransaccion flujo = new FlujoTransaccion();
        flujo.setTransaccionId(transaccion);
        flujo.setEstadoId(estadoId);
        flujo.setComentario(new Date().toString());
        em.persist(flujo);
        respuesta.setResponse(transaccion.getId());
        respuesta.setMensaje(Mensajes.carritoCheckout.replace("%id%", String.valueOf(transaccion.getId())));
        List<Carrito> carro = em.createNamedQuery("Carrito.findByClientId", Carrito.class)
                .setParameter("id", id)
                .getResultList();
        for (Carrito obj : carro) {
            DetalleTransaccion detalle = new DetalleTransaccion();
            detalle.setProductoId(obj.getProductoId());
            detalle.setPrecio(obj.getPrecio());
            detalle.setTransaccionId(transaccion);
            em.persist(detalle);
            em.remove(obj);
        }
        return respuesta;
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader remove(@PathParam("id") Integer id) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.carritoEliminado);
        super.remove(super.find(id));
        this.logger.log(Level.INFO, "Se elimino del carrito", id);
        return respuesta;
    }

    @DELETE
    @Path("all/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader removeAll(@PathParam("id") Integer id) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.carritoAllEliminado);
        List<Carrito> lista = em.createNamedQuery("Carrito.findByClientId", Carrito.class)
                .setParameter("id", id)
                .getResultList();
        for (Carrito linea : lista) {
            super.remove(linea);
        }
        this.logger.log(Level.INFO, "Se elimino todo el carrito", id);
        return respuesta;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Carrito> byuser(@PathParam("id") Integer id) {
        return em.createNamedQuery("Carrito.findByClientId", Carrito.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
