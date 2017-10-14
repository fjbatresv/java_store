/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import com.javastore.dtos.ResponseHeader;
import com.javastore.entities.Categoria;
import com.javastore.entities.Existencia;
import com.javastore.entities.Producto;
import com.javastore.entities.ProductoCategoria;
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
@Path("producto")
public class ProductoFacadeREST extends AbstractFacade<Producto> {

    @PersistenceContext(unitName = "BackendPU")
    private EntityManager em;

    public ProductoFacadeREST() {
        super(Producto.class);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseHeader add(Producto entity) {
        ResponseHeader respuesta = new ResponseHeader();
        List<Producto> valid = em.createNamedQuery("Producto.findByCodigo", Producto.class)
                .setParameter("codigo", entity.getCodigo())
                .getResultList();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.productoCreado);
        if (valid.isEmpty()) {
            super.create(entity);
        } else {
            respuesta.setCodigo(1);
            respuesta.setResultado(false);
            respuesta.setMensaje(Mensajes.productoNoCreado);
            this.logger.log(Level.WARNING, "Producto con codigo repetido", entity);
        }
        respuesta.setResponse(entity);
        return respuesta;
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public ResponseHeader edit(@PathParam("id") Integer id, Producto entity) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.productoEditado);
        List<Producto> valid = em.createNamedQuery("Producto.findByCodigo", Producto.class)
                .setParameter("codigo", entity.getCodigo())
                .getResultList();
        if ((valid.size() > 0 && valid.get(0).getId() == entity.getId()) || valid.isEmpty()) {
            super.edit(entity);
        } else {
            respuesta.setCodigo(1);
            respuesta.setResultado(false);
            respuesta.setMensaje(Mensajes.productoNoEditado);
        }
        respuesta.setResponse(entity);
        return respuesta;
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    public ResponseHeader remove(@PathParam("id") Integer id) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.productoEliminado);
        try {
            super.remove(super.find(id));
            this.logger.log(Level.INFO, "Producto eliminado", id);
        } catch (Exception ex) {
            respuesta.setCodigo(1);
            respuesta.setResultado(false);
            respuesta.setMensaje(Mensajes.productoNoEliminado);
            this.logger.log(Level.WARNING, "No se pudo eliminar producto", ex);
        }
        return respuesta;
    }

    @GET
    @Path("by-categoria/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Producto> byCategoria(@PathParam("id") Integer id) {
        return em.createNamedQuery("ProductoCategoria.findProductoByCategoriaId", Producto.class)
                .setParameter("id", id)
                .getResultList();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("categorias/{id}")
    public List<Categoria> getCategorias(@PathParam("id") Integer id) {
        return em.createNamedQuery("ProductoCategoria.findCategoriaByProductoId", Categoria.class)
                .setParameter("id", id)
                .getResultList();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("categorias/{id}")
    public ResponseHeader setCategorias(@PathParam("id") Integer id, List<Categoria> categorias) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.productoCategorias);
        Producto producto = super.find(id);
        List<ProductoCategoria> relaciones = em.createNamedQuery("ProductoCategoria.findByProductoId", ProductoCategoria.class)
                .setParameter("id", id)
                .getResultList();
        for (ProductoCategoria relacion : relaciones) {
            em.remove(relacion);
        }
        for (Categoria categoria : categorias) {
            ProductoCategoria relacion = new ProductoCategoria();
            relacion.setProductoId(producto);
            relacion.setCategoriaId(categoria);
            em.persist(relacion);
        }
        this.logger.log(Level.INFO, "Categorias del producto actualizadas", producto);
        return respuesta;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("existencia/{id}")
    public ResponseHeader setExistencia(@PathParam("id") Integer id, Existencia existencia) {
        ResponseHeader respuesta = new ResponseHeader();
        respuesta.setCodigo(0);
        respuesta.setResultado(true);
        respuesta.setMensaje(Mensajes.productoExistencia);
        Producto producto = super.find(id);
        existencia.setProductoId(producto);
        existencia.setFechaHora(new Date());
        em.persist(existencia);
        this.logger.log(Level.INFO, "Existencias actualizadas", existencia);
        return respuesta;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Producto find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Producto> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Producto> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
