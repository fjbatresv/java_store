/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.LoginDTO;
import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Usuario;
import com.javastore.entities.UsuarioMenu;
import com.javastore.utils.Crypto;
import com.javastore.utils.Mensajes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Shirley
 */
@Stateless
@Path("session")
public class SessionFacadeREST {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;
    private static final Logger logger = Logger.getLogger(SessionFacadeREST.class.getName());

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader login(LoginDTO entity) {
        ResponseHeader respuesta = new ResponseHeader();
        try {
            List<Usuario> usuario = em.createNamedQuery("Usuario.login", Usuario.class)
                    .setParameter("email", entity.getEmail())
                    .setParameter("password", Crypto.sha1(entity.getPassword()))
                    .getResultList();
            if (!usuario.isEmpty()) {
                respuesta.setCodigo(0);
                respuesta.setMensaje(Mensajes.bienvenidoAdmin);
                respuesta.setResponse(usuario.get(0).getId().intValue());
                respuesta.setResultado(true);
                this.logger.log(Level.INFO, "Usuario admin bienvenido");
            } else {
                respuesta.setCodigo(1);
                respuesta.setMensaje(Mensajes.usuarioInvalido);
                respuesta.setResponse(null);
                respuesta.setResultado(false);
                this.logger.log(Level.INFO, "Usuario invalido", entity);
            }
        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, "No se pudo encriptar password", ex);
        }
        return respuesta;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Usuario active(@CookieParam("user") Integer id) {
        this.logger.log(Level.INFO, "Usuario activo");
        return em.createNamedQuery("Usuario.findById", Usuario.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @GET
    @Path("menus")
    @Produces({MediaType.APPLICATION_JSON})
    public List<UsuarioMenu> menus(@CookieParam("user") Integer id) {
        this.logger.log(Level.INFO, "Buscar menus del usuario");
        return em.createNamedQuery("UsuarioMenu.findByUid", UsuarioMenu.class)
                .setParameter("uid", id)
                .getResultList();
    }

    protected EntityManager getEntityManager() {
        return em;
    }

}
