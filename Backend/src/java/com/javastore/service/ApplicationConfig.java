/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author fjbatresv
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.javastore.service.CarritoFacadeREST.class);
        resources.add(com.javastore.service.CategoriaFacadeREST.class);
        resources.add(com.javastore.service.ClientSessionFacadeREST.class);
        resources.add(com.javastore.service.ClienteFacadeREST.class);
        resources.add(com.javastore.service.DetalleTransaccionFacadeREST.class);
        resources.add(com.javastore.service.EstadoTransaccionFacadeREST.class);
        resources.add(com.javastore.service.ExistenciaFacadeREST.class);
        resources.add(com.javastore.service.FlujoTransaccionFacadeREST.class);
        resources.add(com.javastore.service.MenuFacadeREST.class);
        resources.add(com.javastore.service.ProductoCategoriaFacadeREST.class);
        resources.add(com.javastore.service.ProductoFacadeREST.class);
        resources.add(com.javastore.service.RolFacadeREST.class);
        resources.add(com.javastore.service.RolMenuFacadeREST.class);
        resources.add(com.javastore.service.SessionFacadeREST.class);
        resources.add(com.javastore.service.TransaccionFacadeREST.class);
        resources.add(com.javastore.service.UsuarioFacadeREST.class);
        resources.add(com.javastore.service.UsuarioRolFacadeREST.class);
    }

}
