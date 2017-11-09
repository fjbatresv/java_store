/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.LoginDTO;
import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Cliente;
import com.javastore.entities.Usuario;
import com.javastore.utils.Crypto;
import com.javastore.utils.Mensajes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author javie
 */
@Stateless
@Path("client-session")
public class ClientSessionFacadeREST {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;
    private static final Logger logger = Logger.getLogger(SessionFacadeREST.class.getName());

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    //Mediante esta funcion se validan los credenciales de un usuario cliente
    public ResponseHeader login(LoginDTO entity) {
        ResponseHeader respuesta = new ResponseHeader();
        try {
            List<Cliente> valid = em.createNamedQuery("Cliente.login", Cliente.class)
                    .setParameter("email", entity.getEmail())
                    .setParameter("password", Crypto.sha1(entity.getPassword()))
                    .getResultList();
            if (!valid.isEmpty()) {
                this.logger.log(Level.INFO, "Cliente inicia sesion", valid);
                respuesta.setCodigo(0);
                respuesta.setMensaje(Mensajes.bienvenidoCliente);
                respuesta.setResponse(valid.get(0).getId().intValue());
                respuesta.setResultado(true);
            } else {
                this.logger.log(Level.WARNING, "Cliente invalido", entity);
                respuesta.setCodigo(1);
                respuesta.setMensaje(Mensajes.clienteInvalido);
                respuesta.setResponse(null);
                respuesta.setResultado(false);
            }
        } catch (Exception ex) {
            this.logger.log(Level.WARNING, "No se pudo revisar el cliente", ex);
        }
        return respuesta;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    //De esta forma se activa un usuario cliente
    public Cliente active(@PathParam("id") Integer id) {
        this.logger.log(Level.INFO, "Cliente activo");
        return em.createNamedQuery("Cliente.findById", Cliente.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    protected EntityManager getEntityManager() {
        return em;
    }
}
