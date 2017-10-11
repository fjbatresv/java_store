/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Menu;
import com.javastore.entities.Rol;
import com.javastore.entities.RolMenu;
import com.javastore.entities.UsuarioRol;
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
@Path("rol")
public class RolFacadeREST extends AbstractFacade<Rol> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public RolFacadeREST() {
        super(Rol.class);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseHeader add(Rol entity) {
        ResponseHeader result = new ResponseHeader();
        List<Rol> roles = em.createNamedQuery("Rol.findByNombre", Rol.class)
                .setParameter("nombre", entity.getNombre())
                .getResultList();
        if (roles.isEmpty()) {
            this.logger.log(Level.INFO, "No existen roles con el nombre ", entity);
            super.create(entity);
            result.setCodigo(0);
            result.setResponse(entity.getId());
            result.setResultado(true);
            result.setMensaje(Mensajes.rolCreado);
            this.logger.log(Level.INFO, "Rol creado", entity);
        } else {
            result.setCodigo(1);
            result.setResponse(null);
            result.setResultado(false);
            result.setMensaje(Mensajes.rolRepetido);
            this.logger.log(Level.WARNING, Mensajes.rolRepetido, entity);
        }
        return result;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader edit(@PathParam("id") Integer id, Rol entity) {
        ResponseHeader result = new ResponseHeader();
        result.setCodigo(0);
        result.setResponse(entity.getId());
        result.setResultado(true);
        result.setMensaje(Mensajes.rolEditado);
        List<Rol> roles = em.createNamedQuery("Rol.findByNombre", Rol.class)
                .setParameter("nombre", entity.getNombre())
                .getResultList();
        if ((roles.size() > 0 && roles.get(0).getNombre().equals(entity.getNombre()))
                || (roles.isEmpty())) {
            this.logger.log(Level.INFO, "No existen roles con el nombre o es el mismo", entity);
            super.edit(entity);
            this.logger.log(Level.INFO, "Rol editado", entity);
        } else {
            result.setCodigo(1);
            result.setResponse(entity.getId());
            result.setResultado(false);
            result.setMensaje(Mensajes.rolNoEditado);
            this.logger.log(Level.WARNING, Mensajes.rolNoEditado, entity);
        }
        return result;
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader remove(@PathParam("id") Integer id) {
        ResponseHeader result = new ResponseHeader();
        result.setCodigo(0);
        result.setResponse(id);
        result.setResultado(true);
        result.setMensaje(Mensajes.rolEliminado);
        try {
            super.remove(super.find(id));
            this.logger.log(Level.INFO, "Removido el rol ", id);
        } catch (Exception ex) {
            result.setCodigo(1);
            result.setResponse(id);
            result.setResultado(false);
            result.setMensaje(Mensajes.rolNoEliminado);
            this.logger.log(Level.WARNING, "No se pudo remover el Rol ", id);
        }
        return result;
    }

    @GET
    @Path("menus/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Menu> menus(@PathParam("id") Integer id) {
        return em.createNamedQuery("RolMenu.findMenusByRolId", Menu.class)
                .setParameter("id", id)
                .getResultList();
    }

    @PUT
    @Path("menus/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public ResponseHeader setMenus(@PathParam("id") Integer id, List<Menu> menus) {
        ResponseHeader respuesta = new ResponseHeader();
        Rol rol = super.find(id);
        List<RolMenu> relaciones = em.createNamedQuery("RolMenu.findByRolId", RolMenu.class)
                .setParameter("id", id)
                .getResultList();
        for (RolMenu relacion : relaciones) {
            em.remove(relacion);
        }
        for (Menu menu : menus) {
            RolMenu relacion = new RolMenu();
            relacion.setMenuId(menu);
            relacion.setRolId(rol);
            em.persist(relacion);
        }
        this.logger.log(Level.INFO, "Menus actualizados para el rol " + String.valueOf(id), menus);
        respuesta.setCodigo(0);
        respuesta.setMensaje(Mensajes.rolMenusDone);
        respuesta.setResultado(true);
        return respuesta;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Rol find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Rol> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Rol> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
