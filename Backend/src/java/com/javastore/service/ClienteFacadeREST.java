/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Cliente;
import com.javastore.entities.Usuario;
import com.javastore.utils.Crypto;
import com.javastore.utils.Mensajes;
import java.security.NoSuchAlgorithmException;
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
 
 */
@Stateless
@Path("cliente")
public class ClienteFacadeREST extends AbstractFacade<Cliente> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public ClienteFacadeREST() {
        super(Cliente.class);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseHeader add(Cliente entity) {
        ResponseHeader respuesta = new ResponseHeader();
        try {
            entity.setPassword(Crypto.sha1(entity.getPassword()));
            entity.setActivo(true);
            List<Cliente> tmp = em.createNamedQuery("Cliente.findByEmail", Cliente.class)
                    .setParameter("email", entity.getEmail())
                    .getResultList();
            if (!tmp.isEmpty()) {
                respuesta.setCodigo(1);
                respuesta.setMensaje(Mensajes.clienteRepetido);
                respuesta.setResponse(null);
                respuesta.setResultado(false);
                this.logger.log(Level.INFO, "Cliente repetido");
            } else {
                super.create(entity);
                respuesta.setCodigo(0);
                respuesta.setMensaje(Mensajes.clienteCreado);
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
    public ResponseHeader edit(@PathParam("id") Integer id, Cliente entity) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResponse(entity);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.clienteEditado);
        super.edit(entity);
        return respuesta;
    }
    
    @PUT
    @Path("active/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader active(@PathParam("id") Integer id) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        Cliente cliente = super.find(id);
        respuesta.setMensaje(cliente.getActivo() ? Mensajes.clienteDesactivado : Mensajes.clienteActivado);
        cliente.setActivo(!cliente.getActivo());
        super.edit(cliente);
        respuesta.setResponse(cliente);
        return respuesta;
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Cliente find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cliente> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cliente> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
