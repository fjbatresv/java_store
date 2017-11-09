/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.dtos;

import java.io.Serializable;

/**
 *
 * @author javie
 * Este objeto simplifica el envio de comentarios o texto plano del frontend al backend
 */
public class TextoDTO implements Serializable{
    private String texto;

    public TextoDTO() {
    }

    public TextoDTO(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    
}
