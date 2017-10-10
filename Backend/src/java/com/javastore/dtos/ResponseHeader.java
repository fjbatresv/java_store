/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.dtos;

import java.io.Serializable;

/**
 *
 * @author fjbatresv
 */
public class ResponseHeader implements Serializable {

    private String mensaje;
    private Boolean resultado;
    private int codigo;
    private Object response;

    public ResponseHeader() {
    }

    public ResponseHeader(String mensaje, Boolean resultado, int codigo, Object response) {
        this.mensaje = mensaje;
        this.resultado = resultado;
        this.codigo = codigo;
        this.response = response;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getResultado() {
        return resultado;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

}
