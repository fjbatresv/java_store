/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javastore.dtos;

import java.io.Serializable;

/**
 *
 * @author 
 * Esta clase permite que tanto el frontend como el backend puedan usar un objeto temporal
 * para compartir informacion
 */
public class CompraDTO implements Serializable {

    private String destino;
    private String tarjeta;

    public CompraDTO() {
    }

    public CompraDTO(String destino, String tarjeta) {
        this.destino = destino;
        this.tarjeta = tarjeta;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }
    
    
}
