/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Rol;
import com.javastore.entities.Usuario;
import com.javastore.entities.UsuarioRol;
import com.javastore.utils.Crypto;
import com.javastore.utils.Mensajes;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
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
    //Agregar usuario administradores validando unico correo
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
    //Edicion de un usuario validadndo cambios de contraseñas para sobreescribirlas
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
    //Intenta eliminar un usuario , de no poderse notifica la razon
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
    @Path("roles/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    //Obtener los roles de un usuario;
    public List<Rol> roles(@PathParam("id") Integer id) {
        Usuario usuario = em.createNamedQuery("Usuario.findById", Usuario.class)
                .setParameter("id", id)
                .getSingleResult();
        return em.createNamedQuery("UsuarioRol.findRolesByUsuario", Rol.class)
                .setParameter("usuarioId", usuario)
                .getResultList();
    }

    @PUT
    @Path("roles/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    //Se eliminan los roles a los que el usuario esta asignado y luego se agregan las nuevas relaciones
    public ResponseHeader setRoles(@PathParam("id") Integer id, List<Rol> roles) {
        ResponseHeader respuesta = new ResponseHeader();
        Usuario usuario = super.find(id);
        List<UsuarioRol> relaciones = em.createNamedQuery("UsuarioRol.findByUsuario", UsuarioRol.class)
                .setParameter("usuarioId", usuario)
                .getResultList();
        for (UsuarioRol relacion : relaciones) {
            em.remove(relacion);
        }
        for (Rol rol : roles) {
            UsuarioRol relacion = new UsuarioRol();
            relacion.setRolId(rol);
            relacion.setUsuarioId(usuario);
            em.persist(relacion);
        }
        this.logger.log(Level.INFO, "Roles actualizados para el usuario " + String.valueOf(id), roles);
        respuesta.setCodigo(0);
        respuesta.setMensaje(Mensajes.usuarioRolesDone);
        respuesta.setResultado(true);
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
