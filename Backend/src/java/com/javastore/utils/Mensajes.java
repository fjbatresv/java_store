/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.utils;

/**
 *
 * @author fjbatresv
 */
public class Mensajes {

    public final static String usuarioRepetido = "Ya existe un usuario con el mismo email";
    public final static String usuarioCreado = "Usuario creado exitosamente";
    public final static String usuarioEditado = "Usuario editado exitosamente";
    public final static String usuarioEliminado = "Usuario eliminado satisfactoriamente";
    public final static String usuarioNoEliminado = "Hubo un problema al eliminar el usuario";
    public final static String usuarioRolesDone = "Se han modificado los roles del usuario";

    public final static String bienvenidoAdmin = "Bienvenido usuario administrador";
    public final static String usuarioInvalido = "Correo o clave invalido";

    public final static String rolCreado = "Rol creado exitosamente";
    public final static String rolRepetido = "Ya existe un rol con este nombre";
    public final static String rolEditado = "Rol editado satisfactoriamente";
    public final static String rolNoEditado = "No hemos podido editar el rol, es posible que exista otro con el mismo nombre";
    public final static String rolEliminado = "Rol eliminado exitosamente";
    public final static String rolNoEliminado = "No hemos podido eliminar el rol, es posible que tenga relaciones.";
    public final static String rolMenusDone = "Se han actualizado los menus del rol";

    public final static String categoriaCreada = "La categoria se ha creado exitosamente";
    public final static String categoriaNoCreada = "Hubo un problema al crear la categoria, puede que exista una con el mismo nombre";
    public final static String categoriaEditada = "La categoria se ha editado correctamente";
    public final static String categoriaNoEditada = "Hubo un problema al editar la categoria. Es posible que exista otra con el mismo nombre";
    public final static String categoriaEliminada = "Se ha eliminado la categoria";
    public final static String categoriaNoEliminada = "Hubo un problema al eliminar la categoria, es posible que este en uso.";

    public final static String productoCreado = "Producto creado exitosamente";
    public final static String productoNoCreado = "No se ha creado el producto, es posible que el codigo este repetido";
    public final static String productoEditado = "Producto editado correctamente";
    public final static String productoNoEditado = "No se pudo editar el producto, es posible que el codigo este repetido";
    public final static String productoEliminado = "El producto ha sido eliminado";
    public final static String productoNoEliminado = "El producto no pudo ser eliminado, es posible que tenga relaciones";
    public final static String productoCategorias = "Se han modificado las categorias del producto";
    public final static String productoExistencia = "Existencias actualizadas";

    public final static String clienteEditado = "Cliente modificado correctamente";
    public final static String clienteDesactivado = "Se ha desactivado el cliente";
    public final static String clienteActivado = "Se ha activado el cliente";

}
